package com.garage_system.Service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.garage_system.Model.User;
import com.garage_system.Repository.UserRepository;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

     public UserService(UserRepository userRepository , PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

     public void RegisterUser(User user) {
          user.setPassword(passwordEncoder.encode(user.getPassword()));
          userRepository.save(user);
     }

}
