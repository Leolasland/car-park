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
import ru.project.carpark.converter.DriverMapper;
import ru.project.carpark.dto.DriverDto;
import ru.project.carpark.entity.Driver;
import ru.project.carpark.entity.Enterprise;
import ru.project.carpark.entity.Manager;
import ru.project.carpark.repository.DriverRepository;
import ru.project.carpark.repository.ManagerRepository;
import ru.project.carpark.utils.Generator;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverService {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;
    private final ManagerRepository managerRepository;

    private static final Pageable PAGEABLE = Pageable.ofSize(50);

    public Page<DriverDto> getAllDrivers(Pageable pageable) {
        pageable = Objects.isNull(pageable) ? PAGEABLE : pageable;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Optional<Manager> managerByName = managerRepository.findManagerByName(name);
        if (managerByName.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }
        Manager manager = managerByName.get();

        Page<Driver> drivers = driverRepository.findAllByEmployerIn(pageable,
                manager.getEnterprises());
        if (drivers.isEmpty()) {
            log.info("Drivers are not found");
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }
        List<DriverDto> driverDtoList = drivers.stream().map(driverMapper::entityToDto).toList();
        return new PageImpl<>(driverDtoList, pageable, drivers.getTotalElements());
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

    public DriverDto findById(Integer id) {
        Optional<Driver> optionalDriver = driverRepository.findById(id);
        if (optionalDriver.isEmpty()) {
            log.info("Driver not found");
            return null;
        }
        return driverMapper.entityToDto(optionalDriver.get());
    }
}
