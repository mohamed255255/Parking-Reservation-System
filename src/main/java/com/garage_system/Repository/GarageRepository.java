package com.garage_system.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.garage_system.Model.Garage;
import com.garage_system.Model.Slot;

@Repository
public interface GarageRepository extends JpaRepository<Garage, Integer> {
    
    @Query("select g from Garage g")
    List<Garage> getAllGarages();

    @Query("SELECT s FROM Slot s WHERE s.garage.id = :garageId")
    List<Slot> findAllSlots(int garageId);
  
} 
