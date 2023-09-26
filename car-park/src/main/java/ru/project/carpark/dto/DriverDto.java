package ru.project.carpark.dto;

import lombok.Data;

@Data
public class DriverDto {

    private Integer id;

    private String name;

    private Integer salary;

    private Integer carId;
}
