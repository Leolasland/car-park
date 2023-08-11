package ru.project.carpark.converter;

import org.mapstruct.Mapper;
import ru.project.carpark.dto.BrandDto;
import ru.project.carpark.entity.Brand;

@Mapper(componentModel = "spring", uses = VehicleMapper.class)
public interface BrandMapper {

    BrandDto entityToDto(Brand brand);

    Brand dtoToEntity(BrandDto brandDto);
}
