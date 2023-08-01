package ru.project.carpark.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.project.carpark.service.VehicleService;


@Controller
@RequestMapping("/car")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("vehicles", vehicleService.getAllVehicles());
        return "car/index";
    }
}
