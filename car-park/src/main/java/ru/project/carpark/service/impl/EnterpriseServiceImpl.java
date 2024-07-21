package ru.project.carpark.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.carpark.converter.EnterpriseMapper;
import ru.project.carpark.dto.EnterpriseDto;
import ru.project.carpark.entity.Driver;
import ru.project.carpark.entity.Enterprise;
import ru.project.carpark.entity.Manager;
import ru.project.carpark.entity.Vehicle;
import ru.project.carpark.repository.EnterpriseRepository;
import ru.project.carpark.repository.ManagerRepository;
import ru.project.carpark.service.EnterpriseService;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ru.project.carpark.utils.Generator.generateString;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnterpriseServiceImpl implements EnterpriseService {

    private final EnterpriseRepository enterpriseRepository;
    private final EnterpriseMapper enterpriseMapper;
    private final ManagerRepository managerRepository;

    private final VehicleServiceImpl vehicleService;

    private final DriverServiceImpl driverService;

    @Transactional
    public List<EnterpriseDto> generateEnterprises(Integer enterpriseCount, Integer carCount,
                                                   Manager manager) {

        List<Enterprise> result = new ArrayList<>();
        for (int i = 0; i < enterpriseCount; i++) {
            Enterprise enterprise = new Enterprise();
            enterprise.setName(generateString());
            enterprise.setCity(generateString());
            enterprise.setManagers(List.of(manager));
            enterprise.setTimezone(ZonedDateTime.now().getZone().getId());
            result.add(enterprise);
        }
        List<Enterprise> enterprises = enterpriseRepository.saveAll(result);
        List<Enterprise> managerEnterprises = manager.getEnterprises();
        managerEnterprises.addAll(result);
        manager.setEnterprises(managerEnterprises);
        managerRepository.save(manager);

        for (Enterprise enterprise : enterprises) {
            List<Vehicle> vehicles = vehicleService.generateVehicles(carCount > 0 ? carCount : 10, enterprise);
            List<Driver> drivers = driverService.generateDrivers(carCount > 0 ? carCount : 10, enterprise);
            List<Driver> activeDrivers = drivers.stream().skip(9)
                    .filter(d -> (drivers.indexOf(d) + 1) % 10 == 0).toList();
            List<Vehicle> activeVehicles = vehicles.stream().skip(9)
                    .filter(v -> (vehicles.indexOf(v) + 1) % 10 == 0).toList();
            activeVehicles.forEach(vehicle -> vehicle.setDrivers(activeDrivers));
            activeDrivers.forEach(d -> d.setCars(activeVehicles));
            enterprise.setVehicles(vehicles);
            enterprise.setDrivers(drivers);
        }

        return result.stream().map(enterpriseMapper::entityToDto).toList();
    }

    @Override
    @Transactional
    public List<EnterpriseDto> findAllByManager(Manager manager) {
        if (manager == null) {
            log.warn("Manager cannot be null");
            return Collections.emptyList();
        }
        List<Enterprise> enterprises = enterpriseRepository.findAllByManagers(manager);
        return enterprises.stream()
                .map(enterpriseMapper::entityToDto).toList();
    }

    @Override
    public Optional<Enterprise> findById(Integer id) {
        return enterpriseRepository.findById(id);
    }
}
