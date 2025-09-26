package com.garage_system.Repository.Admin;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.garage_system.Model.Garage;

@Repository
public interface GarageRepository extends JpaRepository<Garage, Integer> {
    
    @Query("select g from Garage g")
    List<Garage> getAllGarages();
} 