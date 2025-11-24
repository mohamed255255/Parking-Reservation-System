package com.garage_system.Model;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    private double slotWidth;

    private double slotDepth;

    private boolean isEmpty;

    private LocalDateTime startTime ;

    private LocalDateTime endingTime ;

    @ManyToOne
    @JoinColumn(name = "garage_id")
    private Garage garage;

    @OneToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;


    @OneToOne(mappedBy = "slot")
    private Reservation reservation ;

    public Slot(double slotWidth, double slotDepth) {
        this.slotWidth = slotWidth;
        this.slotDepth = slotDepth;
        this.isEmpty = true;
    }

    public boolean getisEmpty(){
        return isEmpty ;
    }
}
