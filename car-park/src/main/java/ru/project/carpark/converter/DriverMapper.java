package ru.project.carpark.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.project.carpark.configuration.DefaultMapperConfig;
import ru.project.carpark.dto.DriverDto;
import ru.project.carpark.entity.Driver;
import ru.project.carpark.entity.Vehicle;

import java.util.List;

@Mapper(config = DefaultMapperConfig.class)
public interface DriverMapper {


    @Mapping(target = "carId", source = "cars", qualifiedByName = "carsId")
    DriverDto entityToDto(Driver driver);

    @Mapping(target = "employer", ignore = true)
    @Mapping(target = "cars", ignore = true)
    Driver dtoToEntity(DriverDto driverDto);

    @Named("carsId")
    default Integer getCarId(List<Vehicle> vehicles) {
        return vehicles.isEmpty() ? null : vehicles.stream().map(Vehicle::getId).findAny().orElse(null);
    }
}
