package com.garage_system.Repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.garage_system.Model.User;

@Repository  
public interface UserRepository extends JpaRepository<User, Integer> {
   
    @Query("SELECT u FROM Users u WHERE u.email = :email")
    Optional<User> findByEmail(String email);

    Optional<User> findByAccountCreationToken(String accountCreationToken);
 
}
