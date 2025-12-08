package com.garage_system.Service.payment;


import com.garage_system.Service.payment.WebhookService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.garage_system.Model.Payment ;
import com.garage_system.Repository.PaymentRepository;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class WebhookService{

    private final PaymentRepository paymentRepository;
    // Inject other services as needed, e.g., EnrollmentService

    @Transactional
    public void processPaymentCallback(Map<String, Object> payload) {
        // TODO: Implement HMAC verification and payload processing
        // Example Steps:
        // 1. Verify HMAC signature
        // 2. Extract payment details from payload
        // 3. Update payment status in the database
        // 4. Perform post-payment actions (e.g., buy items, activate subscription, etc.)

        // Example placeholder logic:
        String success = (String) payload.get("success");
        String paymobOrderId = (String) payload.get("order_id");
/* 

        Payment payment = paymentRepository.findById(paymobOrderId).get();  

        if ("true".equalsIgnoreCase(success)) {
            payment.setStatus(Payment.Status.ACCEPTED);
            paymentRepository.save(payment);
            // TODO: perform other actions that should be done after successful payment
        } else {
            payment.setStatus(Payment.Status.FAILED);
            paymentRepository.save(payment);
        }*/
    }
}
