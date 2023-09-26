package ru.project.carpark.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.carpark.converter.CarMapper;
import ru.project.carpark.dto.CarDto;
import ru.project.carpark.dto.VehicleDto;
import ru.project.carpark.converter.VehicleMapper;
import ru.project.carpark.entity.Brand;
import ru.project.carpark.entity.Manager;
import ru.project.carpark.entity.Vehicle;
import ru.project.carpark.repository.ManagerRepository;
import ru.project.carpark.repository.VehicleRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;
    private final CarMapper carMapper;
    private final BrandService brandService;
    private final ManagerRepository managerRepository;

    public List<VehicleDto> getAllVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        if (vehicles.isEmpty()) {
            log.info("Vehicles not found");
            return new ArrayList<>();
        }
        return vehicles.stream().map(vehicleMapper::entityToDto).toList();
    }

    public List<CarDto> getAllCars() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Optional<Manager> managerByName = managerRepository.findManagerByName(name);
        if (managerByName.isEmpty()) {
            return Collections.emptyList();
        }
        Manager manager = managerByName.get();
        List<Vehicle> vehicles = vehicleRepository.findAll();
        if (vehicles.isEmpty()) {
            log.info("Vehicles not found");
            return new ArrayList<>();
        }
        return vehicles.stream().filter(v -> manager.getEnterprises().contains(v.getCompany()))
                .map(carMapper::entityToDto).toList();
    }

    public VehicleDto findById(Integer id) {
        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(id);
        if (optionalVehicle.isEmpty()) {
            log.info("Vehicles not found");
            return null;
        }
        return vehicleMapper.entityToDto(optionalVehicle.get());
    }

    @Transactional
    public void save(VehicleDto vehicle) {
        log.info("Save {}", vehicle);
        Vehicle entity = vehicleMapper.dtoToEntity(vehicle);
        Brand brand = brandService.findByName(vehicle.getCarBrand());
        entity.setCarBrand(brand);
        log.info("Try save vehicle {}, {}", entity, brand);
        vehicleRepository.save(entity);
    }

    @Transactional
    public void update(Integer id, VehicleDto vehicle) {
        Vehicle entity = vehicleMapper.dtoToEntity(vehicle);
        Brand brand = brandService.findByName(vehicle.getCarBrand());
        entity.setCarBrand(brand);
        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(id);
        if (optionalVehicle.isEmpty()) {
            log.info("Vehicles not found");
        }
        entity.setId(optionalVehicle.get().getId());
        vehicleRepository.save(entity);
    }

    @Transactional
    public void delete(Integer id) {
        vehicleRepository.deleteById(id);
    }
}
