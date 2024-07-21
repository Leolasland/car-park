package ru.project.carpark.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.project.carpark.entity.Brand;
import ru.project.carpark.repository.BrandRepository;
import ru.project.carpark.service.impl.BrandServiceImpl;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BrandServiceImplTests {

    @Mock
    private BrandRepository brandRepository;

    @Test
    void getAllBrandsTest() {
        when(brandRepository.findAll()).thenReturn(Collections.emptyList());

        BrandServiceImpl brandService = new BrandServiceImpl(brandRepository);
        List<Brand> result = brandService.getAllBrandsEntities();
        assertTrue(result.isEmpty());
    }

}
