package com.garage_system.Controller;
import com.garage_system.Service.PaymentService;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/webhook")
@RequiredArgsConstructor

public class WebhookController {

    private final PaymentService paymentService;
    
    // ---------------- SERVER CALLBACK ---------------- //

    @PostMapping("/paymob")
    public ResponseEntity<?> handlePaymobServerCallback(
            @RequestBody Map<String, Object> payload,
            HttpServletRequest request) {

        try {
            paymentService.handlePaymentCallback(payload, request);
            return ResponseEntity.ok("Callback received");
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Error: " + ex.getMessage());
        }
    }
}
