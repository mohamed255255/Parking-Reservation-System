package com.garage_system.repository;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.garage_system.model.Payment;
@Repository
public interface PaymentRepository  extends JpaRepository<Payment , UUID>  {
   
    Optional<Payment> findByPaymentId(UUID paymentId);
}
