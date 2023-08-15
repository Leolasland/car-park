package ru.project.carpark.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.project.carpark.dto.BrandDto;
import ru.project.carpark.dto.VehicleDto;
import ru.project.carpark.service.BrandService;
import ru.project.carpark.service.VehicleService;

import java.util.List;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;
    private final BrandService brandService;

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

    @GetMapping("/car/new")
    public String newCar(@ModelAttribute("vehicle") VehicleDto vehicle, Model model) {
        List<BrandDto> brands = brandService.getAllBrands();
        model.addAttribute("brands", brands);
        return "car/new";
    }

    @PostMapping("/car")
    public String createCar(@ModelAttribute("vehicle") VehicleDto vehicle) {
        vehicleService.save(vehicle);
        return "redirect:/car";
    }

    @GetMapping("/car/{id}")
    public String showCar(@PathVariable("id") Integer id, Model model) {
        VehicleDto vehicle = vehicleService.findById(id);
        model.addAttribute("vehicle", vehicle);
        return "car/show";
    }

    @GetMapping("/car/{id}/edit")
    public String editCar(@PathVariable("id") Integer id, Model model) {
        VehicleDto vehicle = vehicleService.findById(id);
        model.addAttribute("vehicle", vehicle);
        List<BrandDto> brands = brandService.getAllBrands();
        model.addAttribute("brands", brands);
        return "car/edit";
    }

    @PatchMapping("/car/{id}")
    public String updateCar(@ModelAttribute("vehicle") VehicleDto vehicle,
                            @PathVariable("id") Integer id) {
        vehicleService.update(id, vehicle);
        return "redirect:/car";
    }

    @DeleteMapping("/car/{id}")
    public String deleteCar(@PathVariable("id") Integer id) {
        vehicleService.delete(id);
        return "redirect:/car";
    }
}
