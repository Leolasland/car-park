package ru.project.carpark.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.project.carpark.configuration.DefaultMapperConfig;
import ru.project.carpark.dto.VehicleDto;
import ru.project.carpark.entity.Vehicle;

import java.time.ZoneId;

@Mapper(config = DefaultMapperConfig.class)
public interface VehicleMapper {

    @Mapping(target = "carBrand", source = "vehicle.carBrand.name")
    @Mapping(target = "dtBuy", source = "vehicle", qualifiedByName = "defineDate")
    VehicleDto entityToDto(Vehicle vehicle);

    @Mapping(target = "carBrand.name", source = "vehicleDto.carBrand")
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "drivers", ignore = true)
    @Mapping(target = "tracks", ignore = true)
    @Mapping(target = "rides", ignore = true)
    Vehicle dtoToEntity(VehicleDto vehicleDto);

    @Named("defineDate")
    default String defineDate(Vehicle vehicle) {
        ZoneId enterpriseTimeZone = ZoneId.of(vehicle.getCompany().getTimezone());
        return vehicle.getDtBuy().withZoneSameInstant(enterpriseTimeZone).toString();
    }
}
