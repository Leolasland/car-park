package ru.project.carpark.converter;

import org.locationtech.jts.geom.Coordinate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.project.carpark.configuration.DefaultMapperConfig;
import ru.project.carpark.dto.CoordinateDto;
import ru.project.carpark.dto.PointDto;
import ru.project.carpark.dto.RideDto;
import ru.project.carpark.entity.CarTrack;
import ru.project.carpark.entity.Ride;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Mapper(config = DefaultMapperConfig.class)
public interface RideMapper {

    @Mapping(target = "carCoordinates", source = "carCoordinates")
    PointDto entityToDate(CarTrack carTrack);

    @Mapping(target = "startCoordinates", source = "tracks", qualifiedByName = "defineStart")
    @Mapping(target = "endCoordinates", source = "tracks", qualifiedByName = "defineEnd")
    @Mapping(target = "dtStart", source = "tracks", qualifiedByName = "defineDate")
    @Mapping(target = "dtEnd", source = "tracks", qualifiedByName = "defineDate")
    @Mapping(target = "startAddress", ignore = true)
    @Mapping(target = "endAddress", ignore = true)
    RideDto rideEntityToDate(Ride ride);

    @Named("defineStart")
    default CoordinateDto defineStart(List<CarTrack> tracks) {
        CarTrack carTrack = Collections.max(tracks, Comparator.comparing(CarTrack::getDtPoint));
        Coordinate coordinate = carTrack.getCarCoordinates().getCoordinate();
        return new CoordinateDto(coordinate.x, coordinate.y);
    }

    @Named("defineEnd")
    default CoordinateDto defineEnd(List<CarTrack> tracks) {
        CarTrack carTrack = Collections.min(tracks, Comparator.comparing(CarTrack::getDtPoint));
        Coordinate coordinate = carTrack.getCarCoordinates().getCoordinate();
        return new CoordinateDto(coordinate.x, coordinate.y);
    }

    @Named("defineDate")
    default String defineDate(List<CarTrack> tracks) {
        CarTrack carTrack = tracks.get(0);
        ZoneId enterpriseTimeZone = ZoneId.of(carTrack.getVehicle().getCompany().getTimezone());
        return carTrack.getDtPoint().withZoneSameInstant(enterpriseTimeZone).toString();
    }
}
