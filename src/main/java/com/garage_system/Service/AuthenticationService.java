package com.garage_system.Service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.garage_system.DTO.request.RegisterUserDto;
import com.garage_system.Model.User;
import com.garage_system.Repository.UserRepository;
import com.garage_system.mapper.UserMapper;

@Service
public class AuthenticationService {

     private final UserRepository userRepository;
     private final PasswordEncoder passwordEncoder;

     public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
          this.passwordEncoder = passwordEncoder;
          this.userRepository = userRepository;
     }

     public void RegisterUser(RegisterUserDto userDto) {

          User user = UserMapper.toUser(userDto, passwordEncoder);
          Optional<User> foundUser = userRepository.findById(user.getId()) ;
          if(foundUser.isPresent()){
              ///  throw acount already created 
          }
          userRepository.save(user);

     }

}
