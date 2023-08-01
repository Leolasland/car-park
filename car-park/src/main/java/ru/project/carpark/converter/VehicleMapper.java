package ru.project.carpark.converter;

import org.mapstruct.Mapper;
import ru.project.carpark.dto.VehicleDto;
import ru.project.carpark.entity.Vehicle;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    VehicleDto entityToDto(Vehicle vehicle);

    Vehicle dtoToEntity(VehicleDto vehicleDto);
}
