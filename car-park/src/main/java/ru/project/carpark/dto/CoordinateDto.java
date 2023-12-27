package ru.project.carpark.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CoordinateDto {

    private double longitude;

    private double latitude;
}
