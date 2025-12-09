package com.garage_system.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.garage_system.DTO.request.LoginUserDto;
import com.garage_system.DTO.request.RegisterUserDto;
import com.garage_system.Model.PasswordResetToken;
import com.garage_system.Model.User;
import com.garage_system.Repository.UserRepository;
import com.garage_system.Repository.PasswordResetRepository;

import com.garage_system.Service.JWTService;
import com.garage_system.mapper.UserMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthenticationService {

     private final UserRepository userRepository;
     private final PasswordEncoder passwordEncoder;
     private final JWTService jwtService ;
     private final AuthenticationManager authManager;
     private final PasswordResetRepository passwordResetRepository ;
  
     public void RegisterUser(RegisterUserDto userDto) {

          User user = UserMapper.toUser(userDto, passwordEncoder);
          Optional<User> foundUser = userRepository.findById(user.getId()) ;
          if(foundUser.isPresent()){ /// to do : return custom exception later
              throw new  DataIntegrityViolationException("User already registered");
          }
          userRepository.save(user);

     }

     public String loginUser(LoginUserDto userDto){
            authManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));
            return jwtService.generateToken(userDto.getEmail());
     }

     public boolean initiatePasswordReset(String username){
        
          //// find user by username ; write jpql
          String token = UUID.randomUUID().toString();
          LocalDateTime expiry = LocalDateTime.now().plusMinutes(15);
         
          PasswordResetToken passwordResetToken = new PasswordResetToken();
          passwordResetToken.setToken(token);
          passwordResetToken.setExpiryDate(expiry);
          //passwordResetToken.setUser(user);
          passwordResetRepository.save(passwordResetToken);

          /// send mail
          return true ;
     }

}
