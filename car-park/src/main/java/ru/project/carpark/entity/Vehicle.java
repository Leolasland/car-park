package ru.project.carpark.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "vehicle")
@Data
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "price")
    private String price;

    @Column(name = "year_manufacture")
    private String yearManufacture;
}
