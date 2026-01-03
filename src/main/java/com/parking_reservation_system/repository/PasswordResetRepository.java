package com.parking_reservation_system.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.parking_reservation_system.model.PasswordResetToken;

public interface PasswordResetRepository  extends  JpaRepository <PasswordResetToken , Integer>{
    Optional<PasswordResetToken> findByToken(String token);
    Optional<PasswordResetToken> findByUserId(int UserId);

}
