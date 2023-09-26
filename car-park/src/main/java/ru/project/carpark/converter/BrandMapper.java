package ru.project.carpark.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.project.carpark.configuration.DefaultMapperConfig;
import ru.project.carpark.dto.BrandDto;
import ru.project.carpark.entity.Brand;

@Mapper(config = DefaultMapperConfig.class)
public interface BrandMapper {

    BrandDto entityToDto(Brand brand);

    @Mapping(target = "vehicles", ignore = true)
    Brand dtoToEntity(BrandDto brandDto);
}
