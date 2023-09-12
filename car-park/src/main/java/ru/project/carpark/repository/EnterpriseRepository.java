package ru.project.carpark.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.carpark.entity.Enterprise;

public interface EnterpriseRepository extends JpaRepository<Enterprise, Integer> {
}
