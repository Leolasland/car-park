package ru.project.carpark.dto;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class VehicleDto {

    private Integer id;

    private Long price;

    private String yearManufacture;

    private String carBrand;

    private String dtBuy;
}
