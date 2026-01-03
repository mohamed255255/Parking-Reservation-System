package com.garage_system.controller.payment ;
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

import com.garage_system.dto.request.ReservationDto;
import com.garage_system.model.Reservation;
import com.garage_system.model.User;
import com.garage_system.security.CustomUserDetails;
import com.garage_system.service.CustomUserDetailsService;
import com.garage_system.service.JWTService;
import com.garage_system.service.payment.PaymentService;

@RestController
@RequestMapping("/api/payments")
@PreAuthorize("hasAnyRole('USER')")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

  @PostMapping("/card/{id}")
    public ResponseEntity<String> initiateCardPayment(
             HttpServletRequest request,
             @PathVariable("id") int reservationId
    ){
        String keyStr = request.getHeader("x-idempotency-key");

        if (keyStr == null || keyStr.isBlank()) {
            return ResponseEntity.badRequest()
                    .body("Missing x-idempotency-key header");
        }

        UUID idempotencyKey;
        try {
            idempotencyKey = UUID.fromString(keyStr);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body("Invalid Idempotency-Key format. Must be a UUID.");
        }

        String paymentLink =
        paymentService.initiateCardPayment(reservationId, idempotencyKey);

        return ResponseEntity.ok(paymentLink);
    }

}
