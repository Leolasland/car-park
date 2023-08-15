package ru.project.carpark.dto;

import lombok.Data;
import ru.project.carpark.enums.MachineType;

@Data
public class BrandDto {

    private Integer id;

    private String name;

    private MachineType machineType;

    private String tank;

    private String loadCapacity;

    private Integer seatsNumber;
}
