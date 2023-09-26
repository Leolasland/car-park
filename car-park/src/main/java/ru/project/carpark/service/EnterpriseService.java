package ru.project.carpark.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.project.carpark.converter.EnterpriseMapper;
import ru.project.carpark.dto.EnterpriseDto;
import ru.project.carpark.entity.Enterprise;
import ru.project.carpark.entity.Manager;
import ru.project.carpark.repository.EnterpriseRepository;
import ru.project.carpark.repository.ManagerRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnterpriseService {

    private final EnterpriseRepository enterpriseRepository;
    private final EnterpriseMapper enterpriseMapper;
    private final ManagerRepository managerRepository;

    public List<EnterpriseDto> getAllEnterprises() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Optional<Manager> managerByName = managerRepository.findManagerByName(name);
        if (managerByName.isEmpty()) {
            return Collections.emptyList();
        }
        Manager manager = managerByName.get();
        List<Enterprise> enterprises = enterpriseRepository.findAll();
        if (enterprises.isEmpty()) {
            log.info("Enterprises are not found");
            return new ArrayList<>();
        }
        return enterprises.stream().filter(e -> e.getManagers().contains(manager))
                .map(enterpriseMapper::entityToDto).toList();
    }
}
