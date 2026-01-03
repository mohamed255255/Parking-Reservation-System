package com.parking_reservation_system.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
@EntityListeners(AuditingEntityListener.class)
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(name = "slot_number")
    private int slotNumber ;

    @Column(nullable = false)
    private double slotWidth;

    @Column(nullable = false)
    private double slotDepth;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime startTime ;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime endingTime ;

    @Column(name = "qrcode_path"   , nullable = false)
    private String qrCodeImagePath ;
   
    @ManyToOne
    @JoinColumn(name = "garage_id" , nullable = false)
    private Garage garage;

    @OneToOne
    @JoinColumn(name = "vehicle_id" , nullable = true)
    private Vehicle vehicle;

    @OneToOne(mappedBy = "slot")
    private Reservation reservation ;

    public Slot(double slotWidth, double slotDepth) {
        this.slotWidth = slotWidth;
        this.slotDepth = slotDepth;
    }
}
