package ru.project.carpark.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.project.carpark.configuration.DefaultMapperConfig;
import ru.project.carpark.dto.VehicleDto;
import ru.project.carpark.entity.Vehicle;

import java.util.List;

@Mapper(config = DefaultMapperConfig.class)
public interface VehicleMapper {

    @Mapping(target = "carBrand", source = "vehicle.carBrand.name")
    VehicleDto entityToDto(Vehicle vehicle);

    @Mapping(target = "carBrand.name", source = "vehicleDto.carBrand")
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "drivers", ignore = true)
    Vehicle dtoToEntity(VehicleDto vehicleDto);
}
