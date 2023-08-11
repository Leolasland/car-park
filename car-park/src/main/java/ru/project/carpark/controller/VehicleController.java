package ru.project.carpark.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.project.carpark.dto.VehicleDto;
import ru.project.carpark.service.VehicleService;

import java.util.List;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping
    public String getAll() {
        return "index";
    }

    @GetMapping("/car")
    public String getAllCars(Model model) {
        List<VehicleDto> vehicles = vehicleService.getAllVehicles();
        model.addAttribute("vehicles", vehicles);
        return "car/index";
    }
}
