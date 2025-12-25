package com.garage_system.Model;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(nullable = false)
    private LocalTime startingTime;

    @Column(nullable = false)
    private LocalTime endingTime;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne
    @JoinColumn(name = "slot_id", nullable = false)
    private Slot slot;


    @Column(nullable = false)
    @CreationTimestamp
    private LocalDate createdAt;
   
    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDate updatedAt;

    @OneToMany(mappedBy = "reservation")
    private List<Payment> payments ;


    private LocalTime startingTime;

    private LocalTime endingTime;
    
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    public Status status;

    public  enum Status {
        PENDING ,
        COMPLETED
    }


    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
