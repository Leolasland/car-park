package ru.project.carpark.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.project.carpark.entity.Brand;
import ru.project.carpark.repository.BrandRepository;
import ru.project.carpark.service.BrandService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    public List<Brand> getAllBrandsEntities() {
        List<Brand> brands = brandRepository.findAll();
        if (brands.isEmpty()) {
            log.info("Brands are not found");
            return new ArrayList<>();
        }
        return brands;
    }
}
