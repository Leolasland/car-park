package ru.project.carpark.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.project.carpark.BaseTest;
import ru.project.carpark.entity.Brand;
import ru.project.carpark.service.BrandService;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class BrandServiceImplTests extends BaseTest {

    @Autowired
    private BrandService brandService;

    @Test
    void getAllBrandsTest() {
        List<Brand> result = brandService.getAllBrandsEntities();
        assertFalse(result.isEmpty());
    }

    @Test
    void getAllBrandsTestEmpty() {
        brandRepository.deleteAll();
        assertDoesNotThrow(() -> brandService.getAllBrandsEntities());
        List<Brand> result = brandService.getAllBrandsEntities();
        assertTrue(result.isEmpty());
    }

}
