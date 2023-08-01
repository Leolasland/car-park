package ru.project.carpark.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class VehicleDto {

    private UUID id;

    private String price;

    private String yearManufacture;
}
