package ru.project.carpark.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.locationtech.jts.geom.Point;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;


@Entity
@Table(name = "vehicle_track")
@Data
public class CarTrack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Column(columnDefinition = "geometry(Point,4326)", name = "coordinates")
    private Point carCoordinates;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id")
    private Vehicle vehicle;

    @Column(name = "dt_point")
    private ZonedDateTime dtPoint = ZonedDateTime.now(ZoneOffset.UTC);
}
