package com.parking_reservation_system.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.parking_reservation_system.model.User;

@Repository  
public interface UserRepository extends JpaRepository<User, Integer> {
   
    Optional<User> findByEmail(String email);

    Optional<User> findByVerificationCode(String code);

    // I could also leave it as existsByEmail(string email) both ways works
    boolean existsByEmail(@Param("email") String email);
 
}
