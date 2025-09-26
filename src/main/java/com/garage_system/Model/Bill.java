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

    ///setters and getters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public int getVehicleNumber() {
        return vehicleNumber;
    }
    public void setVehicleNumber(int vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
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
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public List<User> getUsers() {
        return users;
    }
    public void setUsers(List<User> users) {
        this.users = users;
    }
    public void addUser(User user) {
        users.add(user);
        user.setBill(this);
    }
    public void removeUser(User user) {
        users.remove(user);
        user.setBill(null);
    }
    


}
