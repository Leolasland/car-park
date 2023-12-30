package ru.project.carpark.dto;

import lombok.Data;

@Data
public class CarTrackDto {

    private Integer id;

    private CoordinateDto carCoordinates;

    private String dtPoint;
}
