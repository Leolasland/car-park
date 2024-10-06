package ru.project.carpark.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.geojson.GeoJsonObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.project.carpark.dto.*;
import ru.project.carpark.service.CarTrackService;
import ru.project.carpark.service.RideService;
import ru.project.carpark.service.VehicleService;

import java.util.List;

@RestController
@RequestMapping("/vehicle")
@RequiredArgsConstructor
@Tag(name = "Контроллер для машин", description = "Управление машинами")
public class CarController {

    private final VehicleService vehicleService;
    private final CarTrackService carTrackService;

    private final RideService rideService;

    @Operation(summary = "Список всех машин", description = "Позволяет получить список всех машин")
    @GetMapping
    public Page<CarDto> allCars(Pageable pageable) {
        return vehicleService.getAllCars(pageable);
    }

    @Operation(summary = "Создать машину", description = "Позволяет создать машину")
    @PostMapping
    public void createVehicle(@RequestBody VehicleDto vehicle) {
        vehicleService.save(vehicle);
    }

    @Operation(summary = "Получить машину по ид", description = "Позволяет получить машину по ид")
    @GetMapping("/{id}")
    public VehicleDto getVehicle(@PathVariable("id") @Parameter(description = "Ид машины") Integer id) {
        return vehicleService.findById(id);
    }

    @Operation(summary = "Обновить машину", description = "Позволяет обновить машину по ид")
    @PatchMapping("/{id}")
    public void updateCar(@PathVariable("id") @Parameter(description = "Ид машины") Integer id,
                          @RequestBody VehicleDto vehicle) {
        vehicleService.update(id, vehicle);
    }

    @Operation(summary = "Удалить машину", description = "Позволяет удалить машину по ид")
    @DeleteMapping("/{id}")
    public void deleteVehicle(@PathVariable("id") @Parameter(description = "Ид машины") Integer id) {
        vehicleService.deleteFromEnterprise(id);
    }

    @Operation(summary = "Список всех поездок машины", description = "Позволяет получить список всех поездок машины по ид в формате json")
    @GetMapping("/{id}/track/JSON")
    public List<CarTrackDto> defaultGetCarTrackByCar(@PathVariable("id") @Parameter(description = "Ид машины") Integer id) {
        return vehicleService.getVehicleTrackByVehicleId(id);
    }

    @Operation(summary = "Список всех поездок машины", description = "Позволяет получить список всех поездок машины по ид в формате geo-json")
    @GetMapping("/{id}/track/GEO_JSON")
    public GeoJsonObject geoGetCarTrackByCar(@PathVariable("id") @Parameter(description = "Ид машины") Integer id) {
        return carTrackService.mapToGeo(vehicleService.getVehicleTrackByVehicleId(id), id);
    }

    @Operation(summary = "Получить трек машины по ид и датам", description = "Позволяет получить трек машины по ид и датам")
    @GetMapping("/{id}/track")
    public List<PointDto> getCarTrackByCarAndDate(@PathVariable("id") @Parameter(description = "Ид машины") Integer id,
                                                  @RequestParam @Parameter(description = "Дата начала поездки") String start,
                                                  @RequestParam @Parameter(description = "Дата окончания поездки") String end) {
        return rideService.getRideByVehicleIdAndDate(id, start, end);
    }

    @Operation(summary = "Получить все поездки машины по ид и датам", description = "Позволяет получить все поездки машины по ид и датам")
    @GetMapping("/{id}/track/all")
    public List<RideDto> getAllCarTrackByCarAndDate(@PathVariable("id") @Parameter(description = "Ид машины") Integer id,
                                                    @RequestParam @Parameter(description = "Дата начала поездки") String start,
                                                    @RequestParam @Parameter(description = "Дата окончания поездки") String end) {
        return rideService.getAllCarTrackByCarAndDate(id, start, end);
    }

    @Operation(summary = "Получить все поездки машины по ид и ид поездки", description = "Позволяет получить координаты машины по ид и ид поездки")
    @GetMapping("/{id}/track/{rideId}")
    public List<CarTrackDto> getRideByID(@PathVariable("id") @Parameter(description = "Ид машины") Integer id,
                                         @PathVariable("rideId") @Parameter(description = "Ид поездки") Integer rideId) {
        return rideService.getRideByID(rideId);
    }
}
