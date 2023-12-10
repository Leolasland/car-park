package ru.project.carpark.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.carpark.entity.Driver;
import ru.project.carpark.entity.Enterprise;

import java.util.List;

public interface DriverRepository extends JpaRepository<Driver, Integer> {

    Page<Driver> findAllByEmployerIn(Pageable pageable, List<Enterprise> enterprises);
}
