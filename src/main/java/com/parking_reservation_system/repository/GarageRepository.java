package com.parking_reservation_system.repository;

import com.parking_reservation_system.model.Garage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GarageRepository extends JpaRepository<Garage, Integer> {}
