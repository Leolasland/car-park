package ru.project.carpark.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.project.carpark.dto.VehicleDto;
import ru.project.carpark.converter.VehicleMapper;
import ru.project.carpark.entity.Vehicle;
import ru.project.carpark.repository.VehicleRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;

    public List<VehicleDto> getAllVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        if (vehicles.isEmpty()) {
            log.info("Vehicles not found");
            return new ArrayList<>();
        }
        return vehicles.stream().map(vehicleMapper::entityToDto).toList();
    }
}
