package ru.project.carpark.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.project.carpark.dto.BrandDto;
import ru.project.carpark.service.BrandService;

import java.util.List;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @GetMapping("/brand")
    public String getAllBrands(Model model) {
        List<BrandDto> brands = brandService.getAllBrands();
        model.addAttribute("brands", brands);
        return "brand/index";
    }
}
