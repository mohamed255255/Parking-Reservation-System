package com.garage_system.Model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id ;

    private double amount ;
    private int vehicleNumber ;
    /// private payment_id as a number or reservation_id as fk ;
    //// add vehicle_id instead of vehcile No
    /// add amount  of money instead of 
    private LocalTime startingTime;
    private LocalTime endingTime;
    private LocalDate date ;

    @OneToMany(mappedBy = "bill")
    private List<User> users = new ArrayList<>() ;



}
