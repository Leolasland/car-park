package ru.project.carpark.controller.view;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.project.carpark.converter.BrandMapper;
import ru.project.carpark.converter.VehicleMapper;
import ru.project.carpark.dto.BrandDto;
import ru.project.carpark.dto.VehicleDto;
import ru.project.carpark.entity.Brand;
import ru.project.carpark.entity.Vehicle;
import ru.project.carpark.repository.BrandRepository;
import ru.project.carpark.repository.VehicleRepository;
import ru.project.carpark.service.VehicleService;

import java.util.List;

@Controller
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class VehicleController {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;
    private final VehicleService vehicleService;
    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;

    @GetMapping("/car")
    public String getAllCars(Model model) {
        List<VehicleDto> vehicles = vehicleRepository.findAll().stream()
                .map(vehicleMapper::entityToDto).toList();
        log.info("Vehicles are {}", vehicles);
        model.addAttribute("vehicles", vehicles);
        return "car/index";
    }

    @PostMapping("/car")
    public String createCar(@ModelAttribute("vehicle") VehicleDto vehicle) {
        log.info("Save {}", vehicle);
        Vehicle entity = vehicleMapper.dtoToEntity(vehicle);
        Brand brand = findByName(vehicle.getCarBrand());
        entity.setCarBrand(brand);
        log.info("Try save vehicle {}, {}", entity, brand);
        vehicleService.save(entity);
        return "redirect:/car";
    }

    @GetMapping("/car/{id}")
    public String showCar(@PathVariable("id") Integer id, Model model) {
        long startTime = System.currentTimeMillis();
        VehicleDto vehicle = vehicleService.findById(id);
        model.addAttribute("vehicle", vehicle);
        long endTime = System.currentTimeMillis();
        log.info("Total execution time: {} ms", (endTime - startTime));
        return "car/show";
    }

    @GetMapping("/car/{id}/edit")
    public String editCar(@PathVariable("id") Integer id, Model model) {
        VehicleDto vehicle = vehicleService.findById(id);
        model.addAttribute("vehicle", vehicle);
        List<BrandDto> brands = findAllBrands();
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

    private Brand findByName(String name) {
        return brandRepository.findBrandByName(name).orElse(null);
    }

    private List<BrandDto> findAllBrands() {
        return brandRepository.findAll().stream()
                .map(brandMapper::entityToDto).toList();
    }
}
