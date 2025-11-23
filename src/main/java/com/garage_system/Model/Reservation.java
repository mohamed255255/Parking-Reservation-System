package com.garage_system.Model;

import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter

public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    private LocalTime startingTime;

    private LocalTime endingTime;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "reservation")
    private List<Vehicle> vehicles;


    @OneToOne
    @JoinColumn(name = "slot_id", nullable = false)
    private Slot slot;


    public Reservation() {}



}
