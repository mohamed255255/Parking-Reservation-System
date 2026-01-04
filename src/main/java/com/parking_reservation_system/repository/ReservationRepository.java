package com.parking_reservation_system.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.parking_reservation_system.model.Reservation;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public interface ReservationRepository  extends JpaRepository <Reservation , Integer> , JpaSpecificationExecutor<Reservation> {
        
    @Query("SELECT r FROM Reservation r WHERE r.garage.id = :garageId " +
           "AND r.slot.id = :slotId AND r.status = :status")
    Optional<Reservation> findActiveReservation(
       int garageId,
       int slotId,
       Reservation.Status status
    );
} 
