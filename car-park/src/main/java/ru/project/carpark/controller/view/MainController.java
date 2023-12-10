package ru.project.carpark.controller.view;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.project.carpark.dto.EnterpriseDto;
import ru.project.carpark.dto.VehicleDto;
import ru.project.carpark.service.EnterpriseService;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class MainController {

    private final EnterpriseService enterpriseService;

    @GetMapping("/login")
    public String signIn() {
        return "login";
    }

    @GetMapping("/index")
    public String getAll() {
        return "index";
    }

    @GetMapping("/index/enterprise")
    public String getEnterprise() {
        return "enterprise";
    }

    @GetMapping("/show/enterprise")
    public String showEnterprise(@RequestParam("id") Integer id, Model model) {
        EnterpriseDto enterprise = enterpriseService.findById(id);
        model.addAttribute("enterprise", enterprise);
        return "enterprise/show";
    }
}
