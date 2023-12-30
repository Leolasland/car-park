package ru.project.carpark.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geojson.*;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.carpark.dto.CarTrackDto;
import ru.project.carpark.dto.openroute.RequestOpenRouteDto;
import ru.project.carpark.entity.CarTrack;
import ru.project.carpark.entity.Ride;
import ru.project.carpark.entity.Vehicle;
import ru.project.carpark.repository.CarTrackRepository;
import ru.project.carpark.repository.RideRepository;
import ru.project.carpark.repository.VehicleRepository;
import ru.project.carpark.utils.Generator;

import java.time.ZonedDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarTrackService {

    private final CarTrackRepository carTrackRepository;
    private final OpenRouteService openRouteService;
    private final VehicleRepository vehicleRepository;
    private final RideService rideService;
    private final ApplicationContext applicationContext;

    public CarTrack findById(Integer id) {
        Optional<CarTrack> optionalCarTrack = carTrackRepository.findById(id);
        if (optionalCarTrack.isEmpty()) {
            log.info("Vehicles not found");
            return null;
        }
        return optionalCarTrack.get();
    }

    @Transactional
    public String saveCoordinates(Integer id, GeoJsonObject track) {
        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(id);
        if (optionalVehicle.isEmpty()) {
            return "Vehicle not found";
        }
        Vehicle vehicle = optionalVehicle.get();
        List<Feature> features = ((FeatureCollection) track).getFeatures();
        if (features == null || features.isEmpty()) {
            return "Wrong params for city or country";
        }
        List<CarTrack> result = new ArrayList<>();
        LineString lineString = (LineString) features.get(0).getGeometry();
        List<LngLatAlt> coordinates = lineString.getCoordinates();
        ZonedDateTime end = ZonedDateTime.now();
        ZonedDateTime start = end.minusSeconds(coordinates.size() * 10L);
        Ride ride = rideService.createRide(vehicle, start, end);
        GeometryFactory geometryFactory = new GeometryFactory();
        for (int i = 0; i < coordinates.size(); i++) {
            CarTrack carTrack = new CarTrack();
            carTrack.setVehicle(vehicle);
            carTrack.setDtPoint(start.plusSeconds(i * 10L));
            carTrack.setCarCoordinates(geometryFactory.createPoint(new Coordinate(coordinates.get(i).getLongitude(),
                    coordinates.get(i).getLatitude())));
            carTrack.setRide(ride);
            result.add(carTrack);
        }
        carTrackRepository.saveAll(result);
        return "Success generation";
    }

    public GeoJsonObject mapToGeo(List<CarTrackDto> trackDtoList, Integer id) {
        List<Feature> features = new ArrayList<>();
        for (CarTrackDto dto : trackDtoList) {
            Feature feature = new Feature();
            feature.setId(dto.getId().toString());
            feature.setGeometry(new Point(dto.getCarCoordinates().getLongitude(), dto.getCarCoordinates().getLatitude()));
            feature.setProperties(Map.of("vehicleId", id, "dtPoint", dto.getDtPoint()));
            features.add(feature);
        }
        FeatureCollection response = new FeatureCollection();
        response.setFeatures(features);
        return response;
    }

    public String generateTrack(Integer id, String country, String city, int range, double point) {
        GeoJsonObject geocode = openRouteService.getGeocode(country, city);
        List<Feature> features = ((FeatureCollection) geocode).getFeatures();
        if (features == null || features.isEmpty()) {
            return "Wrong params for city or country";
        }
        RequestOpenRouteDto requestDto = new RequestOpenRouteDto();
        requestDto.setLocations(convertCoordinates(getCoordinates(features)));
        if (point != 0) {
            requestDto.setInterval(point);
        }
        if (range != 0) {
            int rangeInKm = range * 1000;
            requestDto.setRange(List.of(rangeInKm, rangeInKm - 100));
        }
        GeoJsonObject area = openRouteService.getArea(requestDto);
        List<Feature> areaFeatures = ((FeatureCollection) area).getFeatures();
        if (areaFeatures == null || areaFeatures.isEmpty()) {
            return "Wrong params for city or country";
        }
        GeoJsonObject track = openRouteService.getTrack(getRandomRouteCoordinates(areaFeatures).getFirst(),
                getRandomRouteCoordinates(areaFeatures).getSecond());
        return applicationContext.getBean(CarTrackService.class).saveCoordinates(id, track);
    }

    private List<List<Double>> convertCoordinates(double[] array) {
        List<List<Double>> listOfLists = new ArrayList<>();

        for (int i = 0; i < array.length; i += 2) {
            List<Double> innerList = new ArrayList<>();
            innerList.add(array[i]);
            innerList.add(array[i + 1]);
            listOfLists.add(innerList);
        }
        return listOfLists;
    }

    private double[] getCoordinates(List<Feature> features) {
        Point point = (Point) features.get(0).getGeometry();
        return new double[]{point.getCoordinates().getLongitude(), point.getCoordinates().getLatitude()};
    }

    private Pair<String, String> getRandomRouteCoordinates(List<Feature> features) {
        List<LngLatAlt> randomRange;
        LngLatAlt start;
        LngLatAlt end;
        if (Generator.generateInt() % 2 == 0) {
            Polygon geometry = (Polygon) features.get(0).getGeometry();
            randomRange = geometry.getCoordinates().get(0);
        } else {
            Polygon geometry = (Polygon) features.get(1).getGeometry();
            randomRange = geometry.getCoordinates().get(0);
        }
        if (Generator.generateInt() % 2 == 0) {
            start = randomRange.get(0);
            end = randomRange.get(randomRange.size() / 2);
        } else {
            start = randomRange.get(randomRange.size() / 2);
            end = randomRange.get(0);
        }
        return Pair.of(getCoordinates(start), getCoordinates(end));
    }

    private String getCoordinates(LngLatAlt point) {
        return String.valueOf(point.getLongitude()) + ',' +
                String.valueOf(point.getLatitude());
    }
}
