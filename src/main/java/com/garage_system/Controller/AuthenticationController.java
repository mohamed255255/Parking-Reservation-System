package com.garage_system.Controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.garage_system.DTO.request.LoginUserDto;
import com.garage_system.DTO.request.RegisterUserDto;
import com.garage_system.DTO.request.ResetPasswordDto;
import com.garage_system.DTO.request.VerificationDto;
import com.garage_system.Service.AuthenticationService;

import jakarta.validation.Valid;

@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authService) {
        this.authenticationService = authService;
    }

    @PostMapping("/register")
    public void register(@Valid @RequestBody RegisterUserDto user) {
        authenticationService.RegisterUser(user);  
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginUserDto userDto) {
            String JWTtoken = authenticationService.loginUser(userDto);
            return ResponseEntity.ok(Map.of("token", JWTtoken));
    }   

    @PostMapping("/verify-user")
    public void verifyUser(@RequestBody VerificationDto dto)  {
        try {
          authenticationService.verifyUser(dto);  
        } catch (Exception e) { 
            throw new RuntimeException(e.getMessage());
        }
    }
    
    @PostMapping("/forget-password/{email}")
    public ResponseEntity<String> sendResetPasswordLink(@PathVariable String email) {
        String message = authenticationService.sendResetPasswordLink(email) ;
        return ResponseEntity.ok(message) ;
    }
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassowrd(@RequestBody ResetPasswordDto dto , @PathVariable String token)  {
       
        String message = authenticationService.resetPassword(dto , token) ;
        return ResponseEntity.ok(message) ;
    }
        
}