package ru.project.carpark.dto;

import lombok.Data;

@Data
public class CarDto {

    private Integer id;

    private Long price;

    private String yearManufacture;

    private Integer carBrandId;
}
