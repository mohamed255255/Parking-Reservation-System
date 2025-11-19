package com.garage_system.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.garage_system.Repository.UserRepository;
import com.garage_system.Security.UserPrincipal;

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
        return optionalUser.map(UserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        /// Adapter pattern here 
        // it converts the interface of your User entity into something Spring Security expects.
    }
    
}
