package ru.project.carpark.service;

import ru.project.carpark.entity.Driver;
import ru.project.carpark.entity.Enterprise;

import java.util.List;

public interface DriverService {

    List<Driver> generateDrivers(int count, Enterprise enterprise);
}
