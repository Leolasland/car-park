package ru.project.carpark.service;

import ru.project.carpark.dto.EnterpriseDto;
import ru.project.carpark.entity.Enterprise;
import ru.project.carpark.entity.Manager;

import java.util.List;
import java.util.Optional;

public interface EnterpriseService {

    List<EnterpriseDto> generateEnterprises(Integer enterpriseCount, Integer carCount, Manager manager);

    List<EnterpriseDto> findAllByManager(Manager manager);

    Optional<Enterprise> findById(Integer id);
}
