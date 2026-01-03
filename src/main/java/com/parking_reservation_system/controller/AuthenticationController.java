package com.parking_reservation_system.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.parking_reservation_system.dto.request.EmailVerificationDto;
import com.parking_reservation_system.dto.request.LoginUserDto;
import com.parking_reservation_system.dto.request.RegisterUserDto;
import com.parking_reservation_system.dto.request.ResetPasswordDto;
import com.parking_reservation_system.service.AuthenticationService;

import jakarta.validation.Valid;

@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authService) {
        this.authenticationService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterUserDto user) {
        var dtoResponse = authenticationService.RegisterUser(user);  
        return ResponseEntity.ok(dtoResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginUserDto userDto) {
        String JWTtoken = authenticationService.loginUser(userDto);
        return ResponseEntity.ok(Map.of("token", JWTtoken));
    }   

    @PostMapping("/verify-user")
    public ResponseEntity<?> verifyUser(@RequestBody EmailVerificationDto dto)  {
       var dtoResponse = authenticationService.verifyUser(dto);  
       return ResponseEntity.ok(dtoResponse);

       
    }
    
    @PostMapping("/forget-password/{email}")
    public ResponseEntity<String> sendResetPasswordLink(@PathVariable String email) {
        String message = authenticationService.sendResetPasswordLink(email) ;
        return ResponseEntity.ok(message) ;
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassowrd(@RequestBody @Valid ResetPasswordDto dto , @RequestParam("token") String token)  {
        String message = authenticationService.resetPassword(dto , token) ;
        return ResponseEntity.ok(message) ;
    }
        
}