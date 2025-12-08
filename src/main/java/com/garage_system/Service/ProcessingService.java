package com.garage_system.Service;

import com.garage_system.Model.Payment;
import com.garage_system.Repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProcessingService {

    private final PaymentRepository paymentRepository;

    public Payment fun(String x){
        return new Payment();
    }
    @Transactional
    public void processPaymentCallback(Map<String, Object> payload) {

        // 1. Extract the payment_id coming from Paymob callback
        String paymentId = extractString(payload, "id");
        if (paymentId == null) {
            return; // or log the failure
        }

        // 2. Extract success flag
        Boolean success = extractBoolean(payload, "success");

        // 3. Load payment record
        Payment payment = fun(paymentId);
        
        if (payment == null) {
            return; // or log since you don't want custom exceptions
        }

        // 4. Update status
        if (Boolean.TRUE.equals(success)) {
            payment.setStatus(Payment.Status.ACCEPTED);
        } else {
            payment.setStatus(Payment.Status.FAILED);
        }

        paymentRepository.save(payment);
    }

    private String extractString(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString() : null;
    }

    private Boolean extractBoolean(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof String) {
            return Boolean.parseBoolean((String) value);
        }
        return null;
    }
}
