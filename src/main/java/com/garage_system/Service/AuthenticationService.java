package com.garage_system.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.garage_system.DTO.request.LoginUserDto;
import com.garage_system.DTO.request.VerificationDto;

import com.garage_system.DTO.request.RegisterUserDto;
import com.garage_system.Model.PasswordResetToken;
import com.garage_system.Model.User;
import com.garage_system.Repository.PasswordResetRepository;
import com.garage_system.Repository.UserRepository;
import com.garage_system.exception.ResourceNotFoundException;
import com.garage_system.mapper.UserMapper;

import java.nio.file.attribute.UserPrincipal;
import java.security.SecureRandom;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthenticationService {

     private final UserRepository userRepository;
     private final PasswordEncoder passwordEncoder;
     private final JWTService jwtService ;
     private final AuthenticationManager authManager;
    // private final PasswordResetRepository passwordResetRepository ;
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

   public void verifyUser(VerificationDto dto) throws BadRequestException {
          User existingUser = userRepository.findByEmail(dto.getEmail())
               .orElseThrow(() -> new ResourceNotFoundException("User not found"));

          if (existingUser.getExpirationTime().isBefore(LocalDateTime.now())) {
             throw new BadRequestException("Verification code has expired");
          }
 
          if(existingUser.getVerificationCode().equals(dto.getVerificationCode())){
               existingUser.setVerified(true);
               userRepository.save(existingUser) ;
          }else{
               throw new BadRequestException("Invalid verification code") ;
          }
        return ;
     }

     public void resendVerificationCode(String email) throws BadRequestException {
          User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Email not found"));

          if (user.isVerified()) {
               throw new BadRequestException("User is already verified");
          }
          String code = String.format("%05d", RANDOM.nextInt(100_000));

          user.setVerificationCode(code);
          user.setExpirationTime(LocalDateTime.now().plusMinutes(15));
          userRepository.save(user);
          emailService.sendVerificationEmail(email, code);
     }


     public String loginUser(LoginUserDto userDto){
            authManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));
            /// if token expred redirect 
            /// if user is not verified dont login
            return jwtService.generateToken(userDto.getEmail());
     }


     /*
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
 */
}
