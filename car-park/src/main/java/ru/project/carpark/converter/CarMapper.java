package ru.project.carpark.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.project.carpark.configuration.DefaultMapperConfig;
import ru.project.carpark.dto.CarDto;
import ru.project.carpark.entity.Vehicle;

@Mapper(config = DefaultMapperConfig.class)
public interface CarMapper {

    @Mapping(target = "carBrandId", source = "vehicle.carBrand.id")
    CarDto entityToDto(Vehicle vehicle);
}
