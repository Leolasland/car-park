package ru.project.carpark.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.project.carpark.configuration.DefaultMapperConfig;
import ru.project.carpark.dto.CarTrackDto;
import ru.project.carpark.entity.CarTrack;
import ru.project.carpark.entity.Vehicle;

import java.time.ZoneId;

@Mapper(config = DefaultMapperConfig.class)
public interface CarTrackMapper {

    @Mapping(target = "dtPoint", source = "carTrack", qualifiedByName = "defineDate")
    CarTrackDto entityToDto(CarTrack carTrack);

    @Named("defineDate")
    default String defineDate(CarTrack carTrack) {
        ZoneId enterpriseTimeZone = ZoneId.of(carTrack.getVehicle().getCompany().getTimezone());
        return carTrack.getDtPoint().withZoneSameInstant(enterpriseTimeZone).toString();
    }
}
