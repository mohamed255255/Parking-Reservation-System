package com.garage_system.Controller.payment;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import com.garage_system.Service.payment.WebhookService;

@RestController
@RequestMapping("/api/webhook")
public class WebhookController {

    private final WebhookService webhookService;

    public WebhookController(WebhookService webhookService ){
        this.webhookService = webhookService ;
    }

    @PostMapping("/paymob/callback")
    public ResponseEntity<?> handlePaymobCallback(
            @RequestBody Map<String, Object> payload,
            HttpServletRequest request) {
         try {
          
            webhookService.callbackValidation(payload, request); /// handle excpetion in global
            return ResponseEntity.ok(webhookService.processPaymentCallback(payload));     ///  handle excpetion in global
           
        } catch (Exception e) {
            // Handle exceptions aappropriately
            return ResponseEntity.badRequest().body(e.getMessage());
        } 
    }

    @GetMapping("/paymob/response")
    public ResponseEntity<Object> handlePaymentResponse(
            @RequestParam Map<String, String> queryParams) {
       
                // Extract transaction details from queryParams
        String success = queryParams.get("success");
        String message = "Payment " + ("true".equalsIgnoreCase(success) ? "Successful!" : "Failed.");

        // Optionally, verify the HMAC signature here for security

        // Return a response or redirect to a frontend page

        return ResponseEntity.ok(queryParams);
    }
}
