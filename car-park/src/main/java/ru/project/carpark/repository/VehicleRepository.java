package ru.project.carpark.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.carpark.entity.Vehicle;

import java.util.UUID;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {
}
