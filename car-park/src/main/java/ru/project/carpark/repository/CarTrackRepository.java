package ru.project.carpark.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.carpark.entity.CarTrack;

public interface CarTrackRepository extends JpaRepository<CarTrack, Integer> {
}
