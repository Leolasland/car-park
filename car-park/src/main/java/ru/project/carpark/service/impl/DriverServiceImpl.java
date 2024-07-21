package ru.project.carpark.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.carpark.entity.Driver;
import ru.project.carpark.entity.Enterprise;
import ru.project.carpark.repository.DriverRepository;
import ru.project.carpark.service.DriverService;
import ru.project.carpark.utils.Generator;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;

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
