package com.garage_system.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.garage_system.Model.PasswordResetToken;

public interface PasswordResetRepository  extends  JpaRepository <PasswordResetToken , Integer>{
    
}
