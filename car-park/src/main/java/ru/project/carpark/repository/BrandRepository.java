package ru.project.carpark.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.carpark.entity.Brand;

public interface BrandRepository extends JpaRepository<Brand, Integer> {
}
