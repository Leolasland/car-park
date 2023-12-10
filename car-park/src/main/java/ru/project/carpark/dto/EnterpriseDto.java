package ru.project.carpark.dto;

import lombok.Data;

import java.time.ZoneId;
import java.util.List;

@Data
public class EnterpriseDto {

    private Integer id;

    private String name;

    private String city;

    private String timezone;

    private List<Integer> vehicles;

    private List<Integer> drivers;
}
