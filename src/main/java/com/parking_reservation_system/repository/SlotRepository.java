package com.parking_reservation_system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.parking_reservation_system.model.Reservation;
import com.parking_reservation_system.model.Slot;

import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Integer> {
  
    @Transactional
    @Query("""
        SELECT s
        FROM Slot s
        JOIN FETCH s.vehicle v
        JOIN FETCH v.user u
        WHERE u.id = :userId
     """)

    List<Slot> getUserSlotsAndVehicles(int userId);
    
    @Query("SELECT s from Slot s where s.slotNumber=:slotNumber") 
    Optional<Slot> findBySlotNumber(int slotNumber);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Slot s where s.id = :id")
    Optional<Slot> findByIdWithALock(@Param("id") int id);
} 