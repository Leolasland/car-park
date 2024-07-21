package ru.project.carpark.controller.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.project.carpark.converter.EnterpriseMapper;
import ru.project.carpark.dto.EnterpriseDto;
import ru.project.carpark.entity.Enterprise;
import ru.project.carpark.entity.Manager;
import ru.project.carpark.exception.BadRequestException;
import ru.project.carpark.repository.EnterpriseRepository;
import ru.project.carpark.repository.ManagerRepository;
import ru.project.carpark.service.EnterpriseService;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/enterprise")
@RequiredArgsConstructor
@Slf4j
public class EnterpriseController {

    private static final Pageable PAGEABLE = Pageable.ofSize(50);

    private final EnterpriseService enterpriseService;
    private final EnterpriseRepository enterpriseRepository;
    private final ManagerRepository managerRepository;
    private final EnterpriseMapper enterpriseMapper;

    @GetMapping
    public Page<EnterpriseDto> allEnterprises(Pageable pageable) {
        pageable = Objects.isNull(pageable) ? PAGEABLE : pageable;

        Optional<Manager> manager = findManager();
        if (manager.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }
        Page<Enterprise> enterprises = enterpriseRepository.findAllByManagersIn(pageable,
                List.of(manager.get()));
        if (enterprises.isEmpty()) {
            log.info("Enterprises are not found");
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }
        List<EnterpriseDto> enterpriseDtoList = enterprises.stream()
                .map(enterpriseMapper::entityToDto).toList();
        return new PageImpl<>(enterpriseDtoList, pageable, enterprises.getTotalElements());
    }

    @PostMapping
    public List<EnterpriseDto> generateEnterprise(@RequestParam Integer enterpriseCount,
                                                  @RequestParam Integer carCount) {
        if (enterpriseCount < 1) {
            throw new BadRequestException("Enterprise count must be more than 0");
        }
        Optional<Manager> manager = findManager();
        if (manager.isEmpty()) {
            return Collections.emptyList();
        }

        return enterpriseService.generateEnterprises(enterpriseCount, carCount, manager.get());
    }

    private Optional<Manager> findManager() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return managerRepository.findManagerByName(name);
    }
}
