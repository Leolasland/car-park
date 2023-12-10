package ru.project.carpark.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.project.carpark.dto.EnterpriseDto;
import ru.project.carpark.exception.BadRequestException;
import ru.project.carpark.service.EnterpriseService;

import java.util.List;

@RestController
@RequestMapping("/enterprise")
@RequiredArgsConstructor
public class EnterpriseController {

    private final EnterpriseService enterpriseService;

    @GetMapping
    public Page<EnterpriseDto> allEnterprises(Pageable pageable) {
        return enterpriseService.getAllEnterprises(pageable);
    }

    @PostMapping
    public List<EnterpriseDto> generateEnterprise(@RequestParam Integer enterpriseCount,
                                                  @RequestParam Integer carCount) {
        if (enterpriseCount < 1) {
            throw new BadRequestException("Enterprise count must be more than 0");
        }
        return enterpriseService.generateEnterprises(enterpriseCount, carCount);
    }
}
