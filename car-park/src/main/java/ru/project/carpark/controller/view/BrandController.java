package ru.project.carpark.controller.view;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.project.carpark.converter.BrandMapper;
import ru.project.carpark.dto.BrandDto;
import ru.project.carpark.dto.VehicleDto;
import ru.project.carpark.repository.BrandRepository;

import java.util.List;

@Controller
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class BrandController {
    private final BrandMapper brandMapper;
    private final BrandRepository brandRepository;

    @GetMapping("/brand")
    public String getAllBrands(Model model) {
        List<BrandDto> brands = findAllBrands();
        log.info("Brands are {}", brands);
        model.addAttribute("brands", brands);
        return "brand/index";
    }

    @GetMapping("/car/new")
    public String newCar(@ModelAttribute("vehicle") VehicleDto vehicle, Model model) {
        List<BrandDto> brands = findAllBrands();
        log.info("Brands are {}", brands);
        model.addAttribute("brands", brands);
        return "car/new";
    }

    private List<BrandDto> findAllBrands() {
        return brandRepository.findAll().stream()
                .map(brandMapper::entityToDto).toList();
    }
}
