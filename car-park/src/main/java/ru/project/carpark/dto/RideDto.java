package ru.project.carpark.dto;

import lombok.Data;

@Data
public class RideDto {

    private CoordinateDto startCoordinates;

    private CoordinateDto endCoordinates;

    private String startAddress;

    private String endAddress;

    private String dtStart;

    private String dtEnd;
}
