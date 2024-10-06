package ru.project.carpark.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geojson.*;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.carpark.dto.CarTrackDto;
import ru.project.carpark.entity.CarTrack;
import ru.project.carpark.entity.Ride;
import ru.project.carpark.entity.Vehicle;
import ru.project.carpark.repository.CarTrackRepository;
import ru.project.carpark.repository.VehicleRepository;
import ru.project.carpark.service.CarTrackService;
import ru.project.carpark.service.RideService;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarTrackServiceImpl implements CarTrackService {

    private final CarTrackRepository carTrackRepository;
    private final VehicleRepository vehicleRepository;
    private final RideService rideService;

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




}
