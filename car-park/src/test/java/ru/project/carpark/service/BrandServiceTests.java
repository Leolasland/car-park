package ru.project.carpark.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.project.carpark.converter.BrandMapperImpl;
import ru.project.carpark.dto.BrandDto;
import ru.project.carpark.repository.BrandRepository;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BrandServiceTests {

    @Mock
    private BrandRepository brandRepository;

    @Test
    void getAllBrandsTest() {
        when(brandRepository.findAll()).thenReturn(Collections.emptyList());

        BrandService brandService = new BrandService(brandRepository, new BrandMapperImpl());
        List<BrandDto> result = brandService.getAllBrands();
        assertTrue(result.isEmpty());
    }

}
