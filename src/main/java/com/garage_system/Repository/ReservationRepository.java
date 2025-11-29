package com.garage_system.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.garage_system.Model.Reservation;

public interface ReservationRepository  extends JpaRepository <Reservation , Integer> {
          
    
} 
