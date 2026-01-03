package com.garage_system.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.garage_system.dto.request.LoginUserDto;
import com.garage_system.dto.request.RegisterUserDto;
import com.garage_system.dto.request.ResetPasswordDto;
import com.garage_system.dto.request.VerificationDto;
import com.garage_system.exception.ResourceNotFoundException;
import com.garage_system.mapper.UserMapper;
import com.garage_system.model.PasswordResetToken;
import com.garage_system.model.User;
import com.garage_system.repository.PasswordResetRepository;
import com.garage_system.repository.UserRepository;

import jakarta.transaction.Transactional;

import java.security.SecureRandom;

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

     private static final SecureRandom RANDOM = new SecureRandom();

     public void RegisterUser(RegisterUserDto userDto) {
          User user = UserMapper.toUser(userDto, passwordEncoder);    
        
          Optional<User> existingUser = userRepository.findByEmail(userDto.getEmail());
          if(existingUser.isPresent()){ /// to do : return custom exception later
              throw new  DataIntegrityViolationException("User already registered");
          }
          String code = String.format( "%05d" , RANDOM.nextInt(100_000)) ;

          user.setVerificationCode(code);
          user.setVerified(false);
          user.setExpirationTime(LocalDateTime.now().plusMinutes(15));
          userRepository.save(user);
          emailService.sendVerificationEmail(userDto.getEmail() , code);
      
     }

   public void verifyUser(VerificationDto dto) throws RuntimeException {
          User existingUser = userRepository.findByEmail(dto.getEmail())
               .orElseThrow(() -> new ResourceNotFoundException("User not found"));

          if (existingUser.getExpirationTime().isBefore(LocalDateTime.now())) {
             throw new RuntimeException("Verification code has expired");
          }
 
          if(existingUser.getVerificationCode().equals(dto.getVerificationCode())){
               existingUser.setVerified(true);
               userRepository.save(existingUser) ;
          }else{
               throw new RuntimeException("Invalid verification code") ;
          }
        return ;
     }

   public void resendVerificationCode(String email) throws RuntimeException {
          User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Email not found"));

          if (user.isVerified()) {
               throw new RuntimeException("User is already verified");
          }
          String code = String.format("%05d", RANDOM.nextInt(100_000));

          user.setVerificationCode(code);
          user.setExpirationTime(LocalDateTime.now().plusMinutes(15));
          userRepository.save(user);
          emailService.sendVerificationEmail(email, code);
     }


   public String loginUser(LoginUserDto userDto){

          User registeredUser = userRepository.findByEmail(userDto.getEmail())
          .orElseThrow(() -> new ResourceNotFoundException("User not found"));

          if(!registeredUser.isVerified())
                  throw new RuntimeException("account has not verifiyed yet") ;
          
          authManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));
          return jwtService.generateToken(userDto.getEmail());
     }

     //// key generation and saving should be indepndent from sening mail
@Transactional
public PasswordResetToken createResetToken(User user) {
    var optionalToken = passwordResetRepository.findByUserId(user.getId());
    if (optionalToken.isPresent()) {
        PasswordResetToken existingToken = optionalToken.get();
        if (existingToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            existingToken.setToken(UUID.randomUUID().toString());
            existingToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));
            return passwordResetRepository.save(existingToken);
        } else {
            return existingToken; 
        }
    } else {
        PasswordResetToken newToken = new PasswordResetToken();
        newToken.setToken(UUID.randomUUID().toString());
        newToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));
        newToken.setUser(user);
        return passwordResetRepository.save(newToken);
    }
}
    public String sendResetPasswordLink(String email) {
          User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
          PasswordResetToken token = createResetToken(user);
          emailService.sendPasswordResetEmail(email, token.getToken());
          return "Password link has been sent to the user's email: " + email;
     }


     public String resetPassword(ResetPasswordDto dto , String Urltoken){      
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() -> new ResourceNotFoundException("user is not found")) ;
        PasswordResetToken existedToken = passwordResetRepository.findByUserId(user.getId()).orElseThrow(() -> new ResourceNotFoundException("The token doesn't exist")) ;; 
        /// verifying   
        if(!Urltoken.equals(existedToken.getToken())){
            throw new RuntimeException("Token mismatch error");
        }
        /// is expired
        if(existedToken.getExpiryDate().isBefore(LocalDateTime.now())){
             throw new RuntimeException("Reset password token is expired");
        }
        /// same as old password
        if(passwordEncoder.matches(dto.getNewPassword(), user.getPassword())){
               throw new RuntimeException("old password must be different from old password");
        } 
        
     user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
     /// consume after using so it wont be used infinitly
     passwordResetRepository.delete(existedToken); 
     userRepository.save(user) ;      

     return "password is reset successfully" ;
     }

}
