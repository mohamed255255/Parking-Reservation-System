package com.parking_reservation_system.model;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @Column(unique = true , nullable = false)
    private String plateNumber;

    @Column(nullable = false)
    private int modelYear;

    @Column(nullable = false)
    private String modelName;

    @Column(nullable = false)
    private double vehicleWidth;

    @Column(nullable = false)
    private double vehicleDepth;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VehicleType type;

    @OneToOne(mappedBy = "vehicle")
    private Slot slot;

    @ManyToOne
    @JoinColumn(name = "user_id" , nullable = false) 
    private User user ;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDate createdAt;
   
    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDate updatedAt;
  
}
