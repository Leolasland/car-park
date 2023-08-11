package ru.project.carpark.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.carpark.entity.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {
}
