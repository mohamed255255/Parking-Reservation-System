package com.garage_system.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    private String plateNumber;

    private int modelYear;

    private String modelName;

    private double vehicleWidth;

    private double vehicleDepth;

    @Enumerated(EnumType.STRING)
    private VehicleType type;

    @OneToOne(mappedBy = "vehicle")
    private Slot slot;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Reservation reservation ;

  

  
}
