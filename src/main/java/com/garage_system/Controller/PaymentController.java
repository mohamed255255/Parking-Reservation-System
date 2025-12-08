package com.garage_system.Controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.garage_system.Service.PaymentService;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor

public class PaymentController {

    private final PaymentService paymentService;



    @PostMapping("/card")
    public ResponseEntity<Map<String, Object>> initiateCardPayment() {
        Map<String, Object> result = paymentService.initiateCardPayment();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/callback/paymob")
    public ResponseEntity<String> handleCallback(
            @RequestBody Map<String, Object> payload,
            HttpServletRequest request
    ) {
        paymentService.handlePaymentCallback(payload, request);
        return ResponseEntity.ok("Callback Received");
    }
    @GetMapping("/response")
    public ResponseEntity<String> handleRedirect(
            @RequestParam Map<String, String> query
    ) {
        String success = query.get("success");
        return ResponseEntity.ok(
                "Payment " + ("true".equalsIgnoreCase(success) ? "Successful" : "Failed")
        );
    }
}
