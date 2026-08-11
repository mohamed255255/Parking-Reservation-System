package com.parking_reservation_system.controller;

import com.parking_reservation_system.dto.request.EmailVerificationDto;
import com.parking_reservation_system.dto.request.LoginUserDto;
import com.parking_reservation_system.dto.request.RegisterUserDto;
import com.parking_reservation_system.dto.request.ResetPasswordDto;
import com.parking_reservation_system.dto.response.EmailVerificationResponseDto;
import com.parking_reservation_system.dto.response.RegisterUserResponseDto;
import com.parking_reservation_system.service.AuthenticationService;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authService) {
        this.authenticationService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponseDto> register(
            @Valid @RequestBody RegisterUserDto user) {
        RegisterUserResponseDto dtoResponse = authenticationService.RegisterUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginUserDto userDto) {
        String JWTtoken = authenticationService.loginUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("token", JWTtoken));
    }

    @PostMapping("/verify-user")
    public ResponseEntity<EmailVerificationResponseDto> verifyUser(
            @RequestBody EmailVerificationDto dto) {
        EmailVerificationResponseDto dtoResponse = authenticationService.verifyUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoResponse);
    }

    @PostMapping("/forget-password/{email}")
    public ResponseEntity<String> sendResetPasswordLink(@PathVariable String email) {
        String message = authenticationService.sendResetPasswordLink(email);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassowrd(
            @RequestBody @Valid ResetPasswordDto dto, @RequestParam("token") String token) {
        String message = authenticationService.resetPassword(dto, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }
}
