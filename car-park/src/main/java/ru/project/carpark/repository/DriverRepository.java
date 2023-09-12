package ru.project.carpark.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.carpark.entity.Driver;

public interface DriverRepository extends JpaRepository<Driver, Integer> {
}
