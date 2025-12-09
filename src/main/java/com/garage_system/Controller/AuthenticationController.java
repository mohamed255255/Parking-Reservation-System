package com.garage_system.Controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.garage_system.DTO.request.LoginUserDto;
import com.garage_system.DTO.request.RegisterUserDto;
import com.garage_system.Service.AuthenticationService;
import com.garage_system.Service.JWTService;

import jakarta.validation.Valid;

@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authService) {
        this.authenticationService = authService;
    }

    @PostMapping("/register")
    public void register(@Valid @RequestBody RegisterUserDto user) {
        //todo: this method should return custom exception and added to the global place
        authenticationService.RegisterUser(user);  
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginUserDto userDto) {
            String JWTtoken = authenticationService.loginUser(userDto);
            return ResponseEntity.ok(Map.of("token", JWTtoken));
    }   
        
}