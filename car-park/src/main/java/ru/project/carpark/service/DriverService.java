package ru.project.carpark.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.carpark.converter.DriverMapper;
import ru.project.carpark.dto.BrandDto;
import ru.project.carpark.dto.DriverDto;
import ru.project.carpark.entity.*;
import ru.project.carpark.exception.BadRequestException;
import ru.project.carpark.repository.DriverRepository;
import ru.project.carpark.repository.ManagerRepository;
import ru.project.carpark.utils.Generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverService {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;
    private final ManagerRepository managerRepository;

    public List<DriverDto> getAllDrivers() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Optional<Manager> managerByName = managerRepository.findManagerByName(name);
        if (managerByName.isEmpty()) {
            return Collections.emptyList();
        }
        Manager manager = managerByName.get();
        List<Driver> drivers = driverRepository.findAll();
        if (drivers.isEmpty()) {
            log.info("Drivers are not found");
            return new ArrayList<>();
        }
        return drivers.stream().filter(d -> manager.getEnterprises().contains(d.getEmployer()))
                .map(driverMapper::entityToDto).toList();
    }

    @Transactional
    public List<Driver> generateDrivers(int count, Enterprise enterprise) {
        List<Driver> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Driver driver = new Driver();
            driver.setName(Generator.generateString());
            driver.setSalary(Generator.generateInt());
            driver.setEmployer(enterprise);
            result.add(driver);
        }
        driverRepository.saveAll(result);
        return result;
    }
}