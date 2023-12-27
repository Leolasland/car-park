package ru.project.carpark.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.carpark.entity.Ride;
import ru.project.carpark.entity.Vehicle;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface RideRepository extends JpaRepository<Ride, Integer> {

    List<Ride> findAllByVehicleAndDtStartAfterAndDtEndBefore(Vehicle vehicle, ZonedDateTime dtStart, ZonedDateTime dtEnd);

    Optional<Ride> findFirstByOrderByIdDesc();
}
