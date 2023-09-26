package ru.project.carpark.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.project.carpark.dto.CarDto;
import ru.project.carpark.dto.DriverDto;
import ru.project.carpark.service.DriverService;
import ru.project.carpark.service.VehicleService;

import java.util.List;

@RestController
@RequestMapping("/driver")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @GetMapping
    public List<DriverDto> allDrivers() {
        return driverService.getAllDrivers();
    }
}
