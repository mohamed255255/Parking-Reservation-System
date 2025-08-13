package com.garage_system.Model;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalTime startingTime;

    private LocalTime endingTime;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;


    @OneToOne
    @JoinColumn(name = "slot_id", nullable = false)
    private Slot slot;


    public Reservation() {
    }

    public Reservation(LocalTime startingTime, LocalTime endingTime, User user, Vehicle vehicle, Slot slot) {
        this.startingTime = startingTime;
        this.endingTime = endingTime;
        this.user = user;
        this.vehicle = vehicle;
        this.slot = slot;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public LocalTime getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(LocalTime startingTime) {
        this.startingTime = startingTime;
    }

    public LocalTime getEndingTime() {
        return endingTime;
    }

    public void setEndingTime(LocalTime endingTime) {
        this.endingTime = endingTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }
}
