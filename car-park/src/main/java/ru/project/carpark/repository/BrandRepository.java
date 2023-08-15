package ru.project.carpark.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.carpark.entity.Brand;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Integer> {

    Optional<Brand> findBrandByName(String name);
}
