package com.garage_system.Service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.garage_system.DTO.request.UserDto;
import com.garage_system.DTO.request.user.CreateUserDTO;
import com.garage_system.Model.User;
import com.garage_system.Repository.UserRepository;
import com.garage_system.mapper.UserMapper;

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
                              UserDto userDto) {
                                                         
          User user = UserMapper.toUser(userDto , passwordEncoder) ;
          userRepository.save(user);
     }

}
