package com.parking_reservation_system.service;

import com.parking_reservation_system.repository.UserRepository;
import com.parking_reservation_system.security.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

   public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        var optionalUser = userRepository.findByEmail(email);
        return optionalUser
                .map(CustomUserDetails::new)
                .orElseThrow(
                        () -> new UsernameNotFoundException("User not found with email: " + email));
    }
}
