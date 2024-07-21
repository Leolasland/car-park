package ru.project.carpark.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.carpark.entity.Enterprise;
import ru.project.carpark.entity.Manager;

import java.util.List;

public interface EnterpriseRepository extends JpaRepository<Enterprise, Integer> {

    Page<Enterprise> findAllByManagersIn(Pageable pageable, List<Manager> managers);

    List<Enterprise> findAllByManagers(Manager manager);
}
