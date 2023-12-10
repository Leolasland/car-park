package ru.project.carpark.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.carpark.entity.Enterprise;
import ru.project.carpark.entity.Vehicle;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {

    Page<Vehicle> findAllByCompanyIn(Pageable pageable, List<Enterprise> company);
}
