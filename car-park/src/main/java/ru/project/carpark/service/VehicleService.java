package ru.project.carpark.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.project.carpark.dto.CarDto;
import ru.project.carpark.dto.CarTrackDto;
import ru.project.carpark.dto.VehicleDto;
import ru.project.carpark.entity.Enterprise;
import ru.project.carpark.entity.Vehicle;

import java.util.List;

public interface VehicleService {

    Page<CarDto> getAllCars(Pageable pageable);

    VehicleDto findById(Integer id);

    void save(Vehicle vehicle);

    void update(Integer id, VehicleDto vehicle);

    void delete(Integer id);

    List<Vehicle> generateVehicles(int count, Enterprise enterprise);

    void deleteFromEnterprise(Integer id);

    List<CarTrackDto> getVehicleTrackByVehicleId(Integer id);

    List<VehicleDto> getVehicleByEnterprise(Enterprise enterprise);
}
