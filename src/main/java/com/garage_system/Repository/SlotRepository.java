package com.garage_system.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.garage_system.Model.Reservation;
import  com.garage_system.Model.Slot ;

import jakarta.transaction.Transactional;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Integer> {
  
    
    // @Query("SELECT s from slots s where user")
    //List<Slot> findByUserId();
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

} 