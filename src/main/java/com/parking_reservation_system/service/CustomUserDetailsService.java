package com.parking_reservation_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.parking_reservation_system.repository.UserRepository;
import com.parking_reservation_system.security.CustomUserDetails;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository  = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email){
        var optionalUser = userRepository.findByEmail(email);
        return optionalUser.map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        /// Adapter pattern here 
        // it converts the interface of your User entity into something Spring Security expects.
    }

    
    
}
