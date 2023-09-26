package ru.project.carpark.dto;

import lombok.Data;

import java.util.List;

@Data
public class EnterpriseDto {

    private Integer id;

    private String name;

    private String city;

    private List<Integer> vehicles;

    private List<Integer> drivers;
}
