package ru.project.carpark.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.carpark.converter.CarMapper;
import ru.project.carpark.converter.CarTrackMapper;
import ru.project.carpark.dto.BrandDto;
import ru.project.carpark.dto.CarDto;
import ru.project.carpark.dto.CarTrackDto;
import ru.project.carpark.dto.VehicleDto;
import ru.project.carpark.converter.VehicleMapper;
import ru.project.carpark.entity.*;
import ru.project.carpark.exception.BadRequestException;
import ru.project.carpark.repository.ManagerRepository;
import ru.project.carpark.repository.VehicleRepository;
import ru.project.carpark.utils.Generator;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;
    private final CarMapper carMapper;
    private final CarTrackMapper carTrackMapper;
    private final BrandService brandService;
    private final ManagerRepository managerRepository;

    private static final Pageable PAGEABLE = Pageable.ofSize(50);

    public List<VehicleDto> getAllVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        if (vehicles.isEmpty()) {
            log.info("Vehicles not found");
            return new ArrayList<>();
        }
        return vehicles.stream().map(vehicleMapper::entityToDto).toList();
    }

    public Page<CarDto> getAllCars(Pageable pageable) {
        pageable = Objects.isNull(pageable) ? PAGEABLE : pageable;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Optional<Manager> managerByName = managerRepository.findManagerByName(name);
        if (managerByName.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }
        Manager manager = managerByName.get();

        Page<Vehicle> vehicles = vehicleRepository.findAllByCompanyIn(pageable,
                manager.getEnterprises());
        if (vehicles.isEmpty()) {
            log.info("Vehicles not found");
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }
        List<CarDto> carDtoList = vehicles.stream().map(carMapper::entityToDto).toList();
        return new PageImpl<>(carDtoList, pageable, vehicles.getTotalElements());
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
            return;
        }
        entity.setId(optionalVehicle.get().getId());
        vehicleRepository.save(entity);
    }

    @Transactional
    public void delete(Integer id) {
        vehicleRepository.deleteById(id);
    }

    @Transactional
    public List<Vehicle> generateVehicles(int count, Enterprise enterprise) {
        List<Vehicle> result = new ArrayList<>();
        List<Brand> allBrands = brandService.getAllBrandsEntities();
        for (int i = 0; i < count; i++) {
            Vehicle vehicle = new Vehicle();
            vehicle.setPrice(Generator.generateLong());
            vehicle.setYearManufacture(Generator.generateString());
            vehicle.setCarBrand(allBrands.stream().findAny().get());
            vehicle.setCompany(enterprise);
            result.add(vehicle);
        }
        vehicleRepository.saveAll(result);
        return result;
    }

    @Transactional
    public void deleteFromEnterprise(Integer id) {
        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(id);
        if (optionalVehicle.isEmpty()) {
            log.info("Vehicles not found");
        }
        Vehicle vehicle = optionalVehicle.get();
        vehicle.setCompany(null);
        vehicleRepository.save(vehicle);
    }

    public List<CarTrackDto> getVehicleTrackByVehicleId(Integer id) {
        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(id);
        if (optionalVehicle.isEmpty()) {
            log.info("Vehicles not found");
            return Collections.emptyList();
        }
        return optionalVehicle.get().getTracks().stream().map(carTrackMapper::entityToDto)
                .collect(Collectors.toList());
    }
}
