package ru.project.carpark.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.project.carpark.dto.VehicleDto;
import ru.project.carpark.entity.Vehicle;

@Mapper(componentModel = "spring", uses = BrandMapper.class)
public interface VehicleMapper {

    @Mapping(target = "carBrand", source = "vehicle.carBrand.name")
    VehicleDto entityToDto(Vehicle vehicle);


    @Mapping(target = "carBrand.name", source = "vehicleDto.carBrand")
    Vehicle dtoToEntity(VehicleDto vehicleDto);
}
