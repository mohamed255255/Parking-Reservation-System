package com.garage_system.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
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

    @OneToMany(mappedBy = "vehicle")
    private Reservation reservation ;
    public Vehicle() {
    }

    public Vehicle(int id, String plateNumber, int modelYear, String modelName,
                   double vehicleWidth, double vehicleDepth, VehicleType type) {
        this.id = id;
        this.plateNumber = plateNumber;
        this.modelYear = modelYear;
        this.modelName = modelName;
        this.vehicleWidth = vehicleWidth;
        this.vehicleDepth = vehicleDepth;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public int getModelYear() {
        return modelYear;
    }

    public void setModelYear(int modelYear) {
        this.modelYear = modelYear;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public double getVehicleWidth() {
        return vehicleWidth;
    }

    public void setVehicleWidth(double vehicleWidth) {
        this.vehicleWidth = vehicleWidth;
    }

    public double getVehicleDepth() {
        return vehicleDepth;
    }

    public void setVehicleDepth(double vehicleDepth) {
        this.vehicleDepth = vehicleDepth;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }
}
