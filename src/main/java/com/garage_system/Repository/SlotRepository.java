package com.garage_system.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import  com.garage_system.Model.Slot ;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Integer> {
  
    @Query("select s from Slot s")
    List<Slot> getAllSlots();



} 