package ru.project.carpark.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.project.carpark.dto.CarDto;
import ru.project.carpark.dto.VehicleDto;
import ru.project.carpark.service.VehicleService;

import java.util.List;

@RestController
@RequestMapping("/vehicle")
@RequiredArgsConstructor
public class CarController {

    private final VehicleService vehicleService;

    @GetMapping
    public List<CarDto> allCars() {
        return vehicleService.getAllCars();
    }
}
