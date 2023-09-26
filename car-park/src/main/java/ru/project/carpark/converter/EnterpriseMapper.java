package ru.project.carpark.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.project.carpark.configuration.DefaultMapperConfig;
import ru.project.carpark.dto.EnterpriseDto;
import ru.project.carpark.entity.Driver;
import ru.project.carpark.entity.Enterprise;
import ru.project.carpark.entity.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(config = DefaultMapperConfig.class)
public interface EnterpriseMapper {

    @Mapping(target = "vehicles", source = "vehicles", qualifiedByName = "vehiclesId")
    @Mapping(target = "drivers", source = "drivers", qualifiedByName = "driversId")
    EnterpriseDto entityToDto(Enterprise enterprise);

    @Mapping(target = "vehicles", ignore = true)
    @Mapping(target = "drivers", ignore = true)
    @Mapping(target = "managers", ignore = true)
    Enterprise dtoToEntity(EnterpriseDto enterpriseDto);

    @Named("vehiclesId")
    default List<Integer> vehiclesId(List<Vehicle> vehicles){
        return vehicles.stream().map(Vehicle::getId).toList();
    }

    @Named("driversId")
    default List<Integer> driversId(List<Driver> drivers){
        return drivers.stream().map(Driver::getId).toList();
    }
}
