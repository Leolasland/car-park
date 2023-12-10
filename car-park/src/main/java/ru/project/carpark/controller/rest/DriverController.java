package ru.project.carpark.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public Page<DriverDto> allDrivers(Pageable pageable) {
        return driverService.getAllDrivers(pageable);
    }

    @GetMapping("/{id}")
    public DriverDto getDriverById(@PathVariable("id") Integer id) {
        return driverService.findById(id);
    }
}
