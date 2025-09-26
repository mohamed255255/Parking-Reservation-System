package com.garage_system.Model;

import com.garage_system.*;
import com.garage_system.Model.Slot;
import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Entity
public class Garage {
    // garage meta data  :

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String location;
    private boolean isActive;


    /// we code to interface not to concrete classes to achieve polymorphism
    @OneToMany(mappedBy = "garage", cascade = CascadeType.PERSIST)
    private List<Slot> slots = new ArrayList<>();


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
