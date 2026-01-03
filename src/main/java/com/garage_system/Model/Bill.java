package com.garage_system.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @Column(nullable = false)
    private double price ;

    @Column(nullable = false)
    private int vehicleNumber ;

    @Column(nullable = false)
    private LocalTime startingTime;
  
    @Column(nullable = false)
    private LocalTime endingTime;
   
    @Column(nullable = false)
    private LocalDate date ;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDate createdAt;
   
    @Column(nullable = false)
    @UpdateTimestamp
    
    private LocalDate updatedAt;
    @ManyToOne(fetch = FetchType.EAGER)  
    @JoinColumn(name = "user_id" , nullable = false)
    private User user;


     
}
