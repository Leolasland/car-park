package ru.project.carpark.service;

import ru.project.carpark.dto.CarTrackDto;
import ru.project.carpark.dto.PointDto;
import ru.project.carpark.dto.RideDto;
import ru.project.carpark.entity.Ride;
import ru.project.carpark.entity.Vehicle;

import java.time.ZonedDateTime;
import java.util.List;

public interface RideService {

    Ride createRide(Vehicle vehicle, ZonedDateTime start, ZonedDateTime end);

    List<PointDto> getRideByVehicleIdAndDate(Integer id, String start, String end);

    List<RideDto> getAllCarTrackByCarAndDate(Integer id, String start, String end);

    List<RideDto> getAllCarTrackByEnterpriseAndDate(Integer id, String start, String end);

    String processingAddress(double lon, double lan);

    List<CarTrackDto> getRideByID(Integer rideId);

    Double getDistanceByRides(List<RideDto> rideDtoList);
}
