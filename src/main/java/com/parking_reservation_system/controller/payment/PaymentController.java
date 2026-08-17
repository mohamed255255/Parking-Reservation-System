package com.parking_reservation_system.controller.payment;

import com.parking_reservation_system.service.payment.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@PreAuthorize("hasAnyRole('USER')")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/card/{id}")
    public ResponseEntity<String> initiateCardPayment(
            HttpServletRequest request, @PathVariable("id") int reservationId) {
        String keyStr = request.getHeader("x-idempotency-key");

        if (keyStr == null || keyStr.isBlank()) {
            return ResponseEntity.badRequest().body("Missing x-idempotency-key header");
        }

        UUID idempotencyKey;
        try {
            idempotencyKey = UUID.fromString(keyStr);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body("Invalid Idempotency-Key format. Must be a UUID.");
        }

        String paymentLink = paymentService.initiateCardPayment(reservationId, idempotencyKey);

        return ResponseEntity.ok(paymentLink);
    }
}
