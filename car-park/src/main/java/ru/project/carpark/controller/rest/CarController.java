package ru.project.carpark.controller.rest;

import lombok.RequiredArgsConstructor;
import org.geojson.FeatureCollection;
import org.geojson.GeoJsonObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.project.carpark.dto.CarDto;
import ru.project.carpark.dto.CarTrackDto;
import ru.project.carpark.dto.VehicleDto;
import ru.project.carpark.service.CarTrackService;
import ru.project.carpark.service.VehicleService;

import java.util.List;

@RestController
@RequestMapping("/vehicle")
@RequiredArgsConstructor
public class CarController {

    private final VehicleService vehicleService;
    private final CarTrackService carTrackService;

    @GetMapping
    public Page<CarDto> allCars(Pageable pageable) {
        return vehicleService.getAllCars(pageable);
    }

    @PostMapping
    public void createVehicle(VehicleDto vehicle) {
        vehicleService.save(vehicle);
    }

    @GetMapping("/{id}")
    public VehicleDto getVehicle(@PathVariable("id") Integer id) {
        return vehicleService.findById(id);
    }

    @PatchMapping("/{id}")
    public void updateCar(@PathVariable("id") Integer id, VehicleDto vehicle) {
        vehicleService.update(id, vehicle);
    }

    @DeleteMapping("/{id}")
    public void deleteVehicle(@PathVariable("id") Integer id) {
        vehicleService.deleteFromEnterprise(id);
    }

    @GetMapping("/{id}/track/JSON")
    public List<CarTrackDto> defaultGetCarTrackByCar(@PathVariable("id") Integer id) {
        return vehicleService.getVehicleTrackByVehicleId(id);
    }

    @GetMapping("/{id}/track/GEO_JSON")
    public GeoJsonObject geoGetCarTrackByCar(@PathVariable("id") Integer id) {
        return carTrackService.mapToGeo(vehicleService.getVehicleTrackByVehicleId(id), id);
    }
}
