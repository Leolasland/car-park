package ru.project.carpark.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.project.carpark.enums.MachineType;

import java.util.List;

@Entity
@Table(name = "brand")
@Data
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name_brand")
    private String name;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "machine_type")
    private MachineType machineType;

    @Column(name = "tank")
    private String tank;

    @Column(name = "load_capacity")
    private String loadCapacity;

    @Column(name = "seats_number")
    private Integer seatsNumber;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "carBrand")
    private List<Vehicle> vehicles;
}
