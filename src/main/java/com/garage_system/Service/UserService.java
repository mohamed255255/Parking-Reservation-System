package com.garage_system.Service;

import org.hibernate.mapping.UserDefinedObjectType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import com.garage_system.DTO.reqesst.user.CreateUserDTO;
import com.garage_system.Model.User;
import com.garage_system.Repository.UserRepository;

import jakarta.validation.Valid;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

     public UserService(UserRepository userRepository , PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

     public void RegisterUser(@Valid 
                              @RequestBody 
                              CreateUserDTO user) {
                                   
          user.setPassword(passwordEncoder.encode(user.getPassword()));
          userRepository.save(user);
     }

}
