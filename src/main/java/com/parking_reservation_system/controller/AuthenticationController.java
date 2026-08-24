package com.parking_reservation_system.controller;

import com.parking_reservation_system.dto.request.EmailVerificationRequest;
import com.parking_reservation_system.dto.request.LoginUserRequest;
import com.parking_reservation_system.dto.request.RegisterUserRequest;
import com.parking_reservation_system.dto.request.ResetPasswordRequest;
import com.parking_reservation_system.dto.response.EmailVerificationResponse;
import com.parking_reservation_system.dto.response.RegisterUserResponse;
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
    public ResponseEntity<RegisterUserResponse> register(
            @Valid @RequestBody RegisterUserRequest user) {
        RegisterUserResponse dtoResponse = authenticationService.RegisterUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginUserRequest userDto) {
        String JWTtoken = authenticationService.loginUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("token", JWTtoken));
    }

    @PostMapping("/verify-user")
    public ResponseEntity<EmailVerificationResponse> verifyUser(
            @RequestBody EmailVerificationRequest dto) {
        EmailVerificationResponse dtoResponse = authenticationService.verifyUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoResponse);
    }

    @PostMapping("/forget-password/{email}")
    public ResponseEntity<String> sendResetPasswordLink(@PathVariable String email) {
        String message = authenticationService.sendResetPasswordLink(email);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassowrd(  @RequestBody @Valid ResetPasswordRequest dto, @RequestParam("token") String token) {
        String message = authenticationService.resetPassword(dto, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }
}
