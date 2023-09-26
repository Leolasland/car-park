package ru.project.carpark.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.carpark.entity.Manager;

import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager, Integer> {

    Optional<Manager> findManagerByName(String name);
}
