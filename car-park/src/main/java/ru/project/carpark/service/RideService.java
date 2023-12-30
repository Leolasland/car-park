package ru.project.carpark.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.GeoJsonObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.carpark.converter.CarTrackMapper;
import ru.project.carpark.converter.RideMapper;
import ru.project.carpark.dto.CarTrackDto;
import ru.project.carpark.dto.PointDto;
import ru.project.carpark.dto.RideDto;
import ru.project.carpark.entity.CarTrack;
import ru.project.carpark.entity.Ride;
import ru.project.carpark.entity.Vehicle;
import ru.project.carpark.repository.RideRepository;
import ru.project.carpark.repository.VehicleRepository;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RideService {

    private final RideRepository rideRepository;

    private final VehicleRepository vehicleRepository;

    private final RideMapper rideMapper;
    private final OpenRouteService openRouteService;
    private final CarTrackMapper carTrackMapper;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss")
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

    public List<RideDto> getAllCarTrackByCarAndDate(Integer id, String start, String end) {
        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(id);
        if (optionalVehicle.isEmpty()) {
            return Collections.emptyList();
        }
        Vehicle vehicle = optionalVehicle.get();

        if (start != null || end != null) {
            start = start.replace('T', '_');
            end = end.replace('T', '_');
        }

        ZonedDateTime dtStart = ZonedDateTime.parse(start, FORMATTER);
        ZonedDateTime dtEnd = ZonedDateTime.parse(end, FORMATTER);

        List<Ride> rides = rideRepository.findAllByVehicleAndDtStartAfterAndDtEndBefore(vehicle, dtStart, dtEnd);
        List<RideDto> rideDtoList = rides.stream().map(rideMapper::rideEntityToDate).toList();
        for (RideDto ride : rideDtoList) {
            String startAddress = processingAddress(ride.getStartCoordinates().getLongitude(), ride.getStartCoordinates().getLatitude());
            String endAddress = processingAddress(ride.getEndCoordinates().getLongitude(), ride.getEndCoordinates().getLatitude());
            ride.setStartAddress(startAddress);
            ride.setEndAddress(endAddress);
        }
        return rideDtoList;
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
}
