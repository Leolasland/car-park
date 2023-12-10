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
import ru.project.carpark.converter.EnterpriseMapper;
import ru.project.carpark.dto.EnterpriseDto;
import ru.project.carpark.entity.Driver;
import ru.project.carpark.entity.Enterprise;
import ru.project.carpark.entity.Manager;
import ru.project.carpark.entity.Vehicle;
import ru.project.carpark.repository.EnterpriseRepository;
import ru.project.carpark.repository.ManagerRepository;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import static ru.project.carpark.utils.Generator.generateString;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnterpriseService {

    private final EnterpriseRepository enterpriseRepository;
    private final EnterpriseMapper enterpriseMapper;
    private final ManagerRepository managerRepository;

    private final VehicleService vehicleService;

    private final DriverService driverService;

    private static final Pageable PAGEABLE = Pageable.ofSize(50);

    public Page<EnterpriseDto> getAllEnterprises(Pageable pageable) {
        pageable = Objects.isNull(pageable) ? PAGEABLE : pageable;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Optional<Manager> managerByName = managerRepository.findManagerByName(name);
        if (managerByName.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }
        Manager manager = managerByName.get();

        Page<Enterprise> enterprises = enterpriseRepository.findAllByManagersIn(pageable,
                List.of(manager));
        if (enterprises.isEmpty()) {
            log.info("Enterprises are not found");
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }
        List<EnterpriseDto> enterpriseDtoList = enterprises.stream()
                .map(enterpriseMapper::entityToDto).toList();
        return new PageImpl<>(enterpriseDtoList, pageable, enterprises.getTotalElements());
    }

    @Transactional
    public List<EnterpriseDto> generateEnterprises(Integer enterpriseCount, Integer carCount) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Optional<Manager> managerByName = managerRepository.findManagerByName(name);
        if (managerByName.isEmpty()) {
            return Collections.emptyList();
        }
        Manager manager = managerByName.get();

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

    public EnterpriseDto findById(Integer id) {
        Optional<Enterprise> byId = enterpriseRepository.findById(id);
        if (byId.isEmpty()) {
            log.info("Enterprise not found");
            return null;
        }
        return enterpriseMapper.entityToDto(byId.get());
    }
}
