package com.garage_system.Model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import static java.time.temporal.ChronoUnit.MINUTES;
import java.lang.Math;
import java.util.List;

@Entity
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;

    private double price ;
    private int vehicleNumber ;


    private LocalTime startingTime;
    private LocalTime endingTime;
    private LocalDate date ;

    @OneToMany(mappedBy = "bill" , cascade = CascadeType.PERSIST )
    private List<User> users = new ArrayList<>() ;

    public Bill(){}


}
