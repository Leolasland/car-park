package ru.project.carpark.controller.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.project.carpark.converter.DriverMapper;
import ru.project.carpark.dto.DriverDto;
import ru.project.carpark.entity.Driver;
import ru.project.carpark.entity.Manager;
import ru.project.carpark.repository.DriverRepository;
import ru.project.carpark.repository.ManagerRepository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/driver")
@RequiredArgsConstructor
@Slf4j
public class DriverController {

    private static final Pageable PAGEABLE = Pageable.ofSize(50);

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;
    private final ManagerRepository managerRepository;

    @GetMapping
    public Page<DriverDto> allDrivers(Pageable pageable) {
        pageable = Objects.isNull(pageable) ? PAGEABLE : pageable;

        Optional<Manager> manager = findManager();
        if (manager.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        Page<Driver> drivers = driverRepository.findAllByEmployerIn(pageable,
                manager.get().getEnterprises());
        if (drivers.isEmpty()) {
            log.info("Drivers are not found");
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }
        List<DriverDto> driverDtoList = drivers.stream().map(driverMapper::entityToDto).toList();
        return new PageImpl<>(driverDtoList, pageable, drivers.getTotalElements());
    }

    @GetMapping("/{id}")
    public DriverDto getDriverById(@PathVariable("id") Integer id) {
        Optional<Driver> optionalDriver = driverRepository.findById(id);
        if (optionalDriver.isEmpty()) {
            log.info("Driver not found");
            return null;
        }
        return driverMapper.entityToDto(optionalDriver.get());
    }

    private Optional<Manager> findManager() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return managerRepository.findManagerByName(name);
    }
}
