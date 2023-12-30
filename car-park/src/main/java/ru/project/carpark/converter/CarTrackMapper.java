package ru.project.carpark.converter;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.project.carpark.configuration.DefaultMapperConfig;
import ru.project.carpark.dto.CarTrackDto;
import ru.project.carpark.dto.CoordinateDto;
import ru.project.carpark.entity.CarTrack;

import java.time.ZoneId;

@Mapper(config = DefaultMapperConfig.class)
public interface CarTrackMapper {

    @Mapping(target = "dtPoint", source = "carTrack", qualifiedByName = "defineDate")
    @Mapping(target = "carCoordinates", source = "carCoordinates", qualifiedByName = "defineCoordinates")
    CarTrackDto entityToDto(CarTrack carTrack);

    @Named("defineDate")
    default String defineDate(CarTrack carTrack) {
        ZoneId enterpriseTimeZone = ZoneId.of(carTrack.getVehicle().getCompany().getTimezone());
        return carTrack.getDtPoint().withZoneSameInstant(enterpriseTimeZone).toString();
    }

    @Named("defineCoordinates")
    default CoordinateDto defineCoordinates(Point carCoordinates) {
        Coordinate coordinate = carCoordinates.getCoordinate();
        return new CoordinateDto(coordinate.x, coordinate.y);
    }
}
