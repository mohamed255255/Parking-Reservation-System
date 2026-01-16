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

    // i could also leave it as existsByEmail(string email) both ways works
    @Query("""
           select case when count(u) > 0 then 
           true else false end 
           from User u
           where u.email =:email         
           """)
    boolean existsByEmail(@Param("email") String email);
 
}
