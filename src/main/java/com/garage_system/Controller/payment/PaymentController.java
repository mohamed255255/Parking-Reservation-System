package com.garage_system.Controller.payment ;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import javax.swing.text.html.Option;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.*;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.garage_system.DTO.request.ReservationDto;
import com.garage_system.Model.Reservation;
import com.garage_system.Model.User;
import com.garage_system.Security.CustomUserDetails;
import com.garage_system.Service.CustomUserDetailsService;
import com.garage_system.Service.JWTService;
import com.garage_system.Service.payment.PaymentService;

@RestController
@RequestMapping("/api/payments")
@PreAuthorize("hasAnyRole('USER')")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

  @PostMapping("/card/{id}") 
    public ResponseEntity<String> initiateCardPayment(
        @PathVariable("id") UUID reservationId, // Changed to UUID for security
        @RequestHeader(value = "x-idempotency-key", required = false) String keyStr) {

    // 1. Validate Idempotency Header
    if (keyStr == null || keyStr.isBlank()) {
        return ResponseEntity.badRequest().body("Missing x-idempotency-key header");
    }

    UUID idempotencyKey;
    try {
        idempotencyKey = UUID.fromString(keyStr);
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body("Invalid Idempotency-Key format. Must be a UUID.");
    }

    // 2. Call Service
    // The service will throw a ResponseStatusException (409) if a race condition occurs
    String paymentLink = paymentService.initiateCardPayment(reservationId, idempotencyKey);
    
    return ResponseEntity.ok(paymentLink);
}

}
