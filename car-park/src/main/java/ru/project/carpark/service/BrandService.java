package ru.project.carpark.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.project.carpark.converter.BrandMapper;
import ru.project.carpark.dto.BrandDto;
import ru.project.carpark.entity.Brand;
import ru.project.carpark.repository.BrandRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrandService {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;

    public List<BrandDto> getAllBrands() {
        List<Brand> brands = brandRepository.findAll();
        if (brands.isEmpty()) {
            log.info("Brands are not found");
            return new ArrayList<>();
        }
        return brands.stream().map(brandMapper::entityToDto).toList();
    }

    public Brand findByName(String name) {
        return brandRepository.findBrandByName(name).orElse(null);
    }

    public List<Brand> getAllBrandsEntities() {
        List<Brand> brands = brandRepository.findAll();
        if (brands.isEmpty()) {
            log.info("Brands are not found");
            return new ArrayList<>();
        }
        return brands;
    }
}
