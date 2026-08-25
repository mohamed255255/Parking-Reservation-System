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

import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    /// TODO : add rate limiter on all methods except reigster 
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authService) {
        this.authenticationService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponse> register(
            @Valid @RequestBody RegisterUserRequest user) {
        RegisterUserResponse dtoResponse = authenticationService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginUserRequest userDto) {
        String jwtToken = authenticationService.loginUser(userDto);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("token", jwtToken));
    }

    @PostMapping("/verify-user")
    public ResponseEntity<EmailVerificationResponse> verifyUser( @Valid @RequestBody EmailVerificationRequest dto) {
        EmailVerificationResponse dtoResponse = authenticationService.verifyUser(dto);
        return ResponseEntity.status(HttpStatus.OK).body(dtoResponse);
    }
    // TODO : email is PII should be sent as object
    @PostMapping("/forget-password/{email}")
    public ResponseEntity<String> sendResetPasswordLink(@PathVariable @NonNull String email) {
        String message = authenticationService.sendResetPasswordLink(email);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(  @RequestBody @Valid ResetPasswordRequest dto) {
        authenticationService.resetPassword(dto);
        return ResponseEntity.status(HttpStatus.OK).body("Password is reset successfully");
    }
}
