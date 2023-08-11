package ru.project.carpark.dto;

import lombok.Data;

@Data
public class BrandDto {

    private Integer id;

    private String name;

    private String machineType;

    private String tank;

    private String loadCapacity;

    private Integer seatsNumber;
}
