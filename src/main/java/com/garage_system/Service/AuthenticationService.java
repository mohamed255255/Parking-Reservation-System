package com.garage_system.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.garage_system.DTO.request.LoginUserDto;
import com.garage_system.DTO.request.RegisterUserDto;
import com.garage_system.Model.PasswordResetToken;
import com.garage_system.Repository.PasswordResetRepository;
import com.garage_system.Repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthenticationService {

     private final UserRepository userRepository;
     private final PasswordEncoder passwordEncoder;
     private final JWTService jwtService ;
     private final AuthenticationManager authManager;
     private final PasswordResetRepository passwordResetRepository ;
     private final EmailService emailService ;
     
     public void RegisterUser(RegisterUserDto userDto) {
          User user = UserMapper.toUser(userDto, passwordEncoder);    
          Optional<User> existingUser = userRepository.findByEmail(userDto.getEmail());
          if(existingUser.isPresent()){ /// to do : return custom exception later
              throw new  DataIntegrityViolationException("User already registered");
          }
          String token = UUID.randomUUID().toString();
          user.setAccountCreationToken(token);
          user.setVerified(false);
          userRepository.save(user);
          emailService.sendVerificationEmail(userDto.getEmail() , token);
      
     }
     public void verifyUser(String token) {
          User user = userRepository
                  .findByAccountCreationToken(token)
                  .orElseThrow(() -> new RuntimeException("Invalid token"));
      
          user.setVerified(true);
          user.setAccountCreationToken(null); // invalidate token
      
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
