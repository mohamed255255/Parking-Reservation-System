package com.garage_system.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.garage_system.Model.Payment;
@Repository
public interface PaymentRepository  extends JpaRepository<Payment , Integer>  {
    
}
