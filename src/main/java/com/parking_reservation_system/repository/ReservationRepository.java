package com.parking_reservation_system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.parking_reservation_system.model.Reservation;
import com.parking_reservation_system.model.Slot;

public interface ReservationRepository  extends JpaRepository <Reservation , Integer> {
        
    @Query("SELECT r FROM Reservation r WHERE r.garage.id = :garageId " +
           "AND r.slot.id = :slotId AND r.status = :status")
    Optional<Reservation> findActiveReservation(
       int garageId,
       int slotId,
       Reservation.Status status
    );
} 
