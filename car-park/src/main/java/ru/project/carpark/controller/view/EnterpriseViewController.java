package ru.project.carpark.controller.view;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.project.carpark.converter.EnterpriseMapper;
import ru.project.carpark.dto.EnterpriseDto;
import ru.project.carpark.repository.EnterpriseRepository;

@Controller
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class EnterpriseViewController {
    private final EnterpriseRepository repository;
    private final EnterpriseMapper mapper;

    @GetMapping("/show/enterprise")
    public String showEnterprise(@RequestParam("id") Integer id, Model model) {
        EnterpriseDto enterprise = repository.findById(id)
                .map(mapper::entityToDto).orElse(null);
        log.info("Result is {}", enterprise);
        model.addAttribute("enterprise", enterprise);
        return "enterprise/show";
    }
}
