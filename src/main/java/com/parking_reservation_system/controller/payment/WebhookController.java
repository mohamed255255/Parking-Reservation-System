package com.parking_reservation_system.controller.payment;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.parking_reservation_system.service.payment.WebhookService;

import java.util.Map;

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
             webhookService.HmacValidation(payload, request);
             webhookService.processPaymentCallback(payload);
          
             return ResponseEntity.ok("Callback recieved sucessfully");     
           
        } catch (Exception e) {
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
