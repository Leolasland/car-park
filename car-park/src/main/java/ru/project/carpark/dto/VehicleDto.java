package ru.project.carpark.dto;

import lombok.Data;

@Data
public class VehicleDto {

    private Integer id;

    private Long price;

    private String yearManufacture;

    private BrandDto carBrand;
}
