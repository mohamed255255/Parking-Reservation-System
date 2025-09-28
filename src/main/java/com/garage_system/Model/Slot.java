package com.garage_system.Model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    private double slotWidth;

    private double slotDepth;

    private boolean empty;

    @ManyToOne
    @JoinColumn(name = "garage_id")
    private Garage garage;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;


    @OneToOne(mappedBy = "slot")
    private Reservation reservation ;

    public Slot() {
    }

    public Slot(double slotWidth, double slotDepth) {
        this.slotWidth = slotWidth;
        this.slotDepth = slotDepth;
        this.empty = true;
    }

    public int getId() {
        return id;
    }

    public double getSlotWidth() {
        return slotWidth;
    }

    public void setSlotWidth(double slotWidth) {
        this.slotWidth = slotWidth;
    }

    public double getSlotDepth() {
        return slotDepth;
    }

    public void setSlotDepth(double slotDepth) {
        this.slotDepth = slotDepth;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public Garage getGarage() {
        return garage;
    }

    public void setGarage(Garage garage) {
        this.garage = garage;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
