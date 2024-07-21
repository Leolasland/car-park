package ru.project.carpark.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.GeoJsonObject;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.carpark.converter.CarTrackMapper;
import ru.project.carpark.converter.RideMapper;
import ru.project.carpark.dto.CarTrackDto;
import ru.project.carpark.dto.PointDto;
import ru.project.carpark.dto.RideDto;
import ru.project.carpark.entity.CarTrack;
import ru.project.carpark.entity.Enterprise;
import ru.project.carpark.entity.Ride;
import ru.project.carpark.entity.Vehicle;
import ru.project.carpark.repository.RideRepository;
import ru.project.carpark.repository.VehicleRepository;
import ru.project.carpark.service.EnterpriseService;
import ru.project.carpark.service.OpenRouteIntegrationService;
import ru.project.carpark.service.RideService;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;

    private final VehicleRepository vehicleRepository;

    private final RideMapper rideMapper;
    private final OpenRouteIntegrationService openRouteService;
    private final EnterpriseService enterpriseService;
    private final CarTrackMapper carTrackMapper;

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss")
            .withZone(ZoneOffset.UTC);
    public static final DateTimeFormatter FORMATTER_WITHOUT_SECONDS = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm")
            .withZone(ZoneOffset.UTC);

    @Transactional
    public Ride createRide(Vehicle vehicle, ZonedDateTime start, ZonedDateTime end) {
        Ride ride = new Ride();
        ride.setVehicle(vehicle);
        ride.setDtStart(start);
        ride.setDtEnd(end);
        return rideRepository.save(ride);
    }


    public List<PointDto> getRideByVehicleIdAndDate(Integer id, String start, String end) {
        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(id);
        if (optionalVehicle.isEmpty()) {
            return Collections.emptyList();
        }
        Vehicle vehicle = optionalVehicle.get();

        if (start != null && end != null) {
            start = start.replace('T', '_');
            end = end.replace('T', '_');
        }

        ZonedDateTime dtStart = ZonedDateTime.parse(start, FORMATTER);
        ZonedDateTime dtEnd = ZonedDateTime.parse(end, FORMATTER);

        List<Ride> rides = rideRepository.findAllByVehicleAndDtStartAfterAndDtEndBefore(vehicle, dtStart, dtEnd);

        List<CarTrack> carTracks = rides.stream().map(Ride::getTracks).toList().stream().flatMap(Collection::stream).toList();

        return carTracks.stream().map(rideMapper::entityToDate).toList();
    }

    @Override
    @Transactional
    @Cacheable(value = "carRides")
    public List<RideDto> getAllCarTrackByCarAndDate(Integer id, String start, String end) {
        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(id);
        if (optionalVehicle.isEmpty()) {
            return Collections.emptyList();
        }
        Vehicle vehicle = optionalVehicle.get();

        if (start == null || end == null || start.isEmpty() || end.isEmpty()) {
            return Collections.emptyList();
        }

        ZonedDateTime dtStart;
        ZonedDateTime dtEnd;
        try {
            dtStart = ZonedDateTime.parse(start.replace('T', '_'), FORMATTER);
            dtEnd = ZonedDateTime.parse(end.replace('T', '_'), FORMATTER);
        } catch (DateTimeParseException ex) {
            try {
                dtStart = ZonedDateTime.parse(start.replace('T', '_'), FORMATTER_WITHOUT_SECONDS);
                dtEnd = ZonedDateTime.parse(end.replace('T', '_'), FORMATTER_WITHOUT_SECONDS);
            } catch (DateTimeParseException e) {
                log.error("Invalid Date format for {} or {}", start, end);
                return Collections.emptyList();
            }
        }

        List<Ride> rides = rideRepository.findAllByVehicleAndDtStartAfterAndDtEndBefore(vehicle, dtStart, dtEnd);
        List<RideDto> rideDtoList = rides.stream()
                .filter(r -> !r.getTracks().isEmpty())
                .map(rideMapper::rideEntityToDate).toList();
        for (RideDto ride : rideDtoList) {
            String startAddress = processingAddress(ride.getStartCoordinates().getLongitude(), ride.getStartCoordinates().getLatitude());
            String endAddress = processingAddress(ride.getEndCoordinates().getLongitude(), ride.getEndCoordinates().getLatitude());
            ride.setStartAddress(startAddress);
            ride.setEndAddress(endAddress);
        }
        return rideDtoList;
    }

    @Override
    @Transactional
    @Cacheable(value = "enterpriseRides")
    public List<RideDto> getAllCarTrackByEnterpriseAndDate(Integer id, String start, String end) {
        Optional<Enterprise> enterpriseOptional = enterpriseService.findById(id);
        if (enterpriseOptional.isEmpty()) {
            return Collections.emptyList();
        }
        List<RideDto> result = new ArrayList<>();
        List<Integer> vehiclesId = enterpriseOptional.get().getVehicles().stream()
                .map(Vehicle::getId).toList();
        for (Integer vehicleId : vehiclesId) {
            result.addAll(getAllCarTrackByCarAndDate(vehicleId, start, end));
        }
        return result;
    }

    public String processingAddress(double lon, double lan) {
        GeoJsonObject geocodeReverse = openRouteService.getGeocodeReverse(lon, lan);
        List<Feature> features = ((FeatureCollection) geocodeReverse).getFeatures();
        return features.get(0).getProperties().get("label").toString();
    }

    public List<CarTrackDto> getRideByID(Integer rideId) {
        Optional<Ride> optionalRide = rideRepository.findById(rideId);
        if (optionalRide.isEmpty()) {
            return Collections.emptyList();
        }
        List<CarTrack> tracks = optionalRide.get().getTracks();
        return tracks.stream().map(carTrackMapper::entityToDto)
                .toList();
    }

    @Override
    @Cacheable(value = "distance")
    public Double getDistanceByRides(List<RideDto> rideDtoList) {
        double result = 0.0;
        for (RideDto ride : rideDtoList) {
            result += calculateDistance(ride.getStartCoordinates().getLatitude(), ride.getStartCoordinates().getLongitude(),
                    ride.getEndCoordinates().getLatitude(), ride.getEndCoordinates().getLongitude());

        }
        return result;
    }

    public static final double RADIUS_OF_EARTH = 6371; // Радиус Земли в км

    private static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.pow(Math.sin(dLat / 2), 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.pow(Math.sin(dLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return RADIUS_OF_EARTH * c;
    }

}
