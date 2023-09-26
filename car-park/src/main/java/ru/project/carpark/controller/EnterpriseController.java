package ru.project.carpark.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.project.carpark.dto.EnterpriseDto;
import ru.project.carpark.service.EnterpriseService;

import java.util.List;

@RestController
@RequestMapping("/enterprise")
@RequiredArgsConstructor
public class EnterpriseController {

    private final EnterpriseService enterpriseService;

    @GetMapping
    public List<EnterpriseDto> allEnterprises() {
        return enterpriseService.getAllEnterprises();
    }
}
