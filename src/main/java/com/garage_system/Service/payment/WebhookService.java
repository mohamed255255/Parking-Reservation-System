package com.garage_system.Service.payment;


import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.garage_system.Model.Payment;
import com.garage_system.Repository.PaymentRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor ;

@Service
@RequiredArgsConstructor
public class WebhookService{

    @Value("${PAYMOB.HMAC_SECRET}")
    private String hmacSecretKey;

    private final PaymentRepository paymentRepository;

    // Additional helper methods like HMAC verification can be added here
    private String concatenateValues(Map<String, Object> payload, List<String> hmacKeys) {
        Map<String, Object> obj = (Map<String, Object>) payload.get("obj");
        if (obj == null) {
            throw new RuntimeException("Invalid payload: missing 'obj' key");
        }

        StringBuilder concatenated = new StringBuilder();

        for (String key : hmacKeys) {
            String[] parts = key.split("\\.");
            Object value = null;

            if (parts.length == 1) {
                // Direct key in 'obj'
                value = obj.get(parts[0]);
            } else if (parts.length == 2) {
                // Nested key in 'obj'
                Object nestedObj = obj.get(parts[0]);
                if (nestedObj instanceof Map) {
                    value = ((Map<?, ?>) nestedObj).get(parts[1]);
                }
            }

            if (value == null) {
                value = ""; // Use empty string if value is null
            } else if (value instanceof Boolean) {
                value = value.toString(); // Convert boolean to string
            } else if (value instanceof Map || value instanceof List) {
                value = value.toString(); // Convert map or list to string
            }

            concatenated.append(value);
        }

        return concatenated.toString();
    }


    private String calculateHmac(String data, String secretKey) {
        try {
            Mac sha512Hmac = Mac.getInstance("HmacSHA512");
            SecretKey keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            sha512Hmac.init(keySpec);
            byte[] macData = sha512Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));

            StringBuilder result = new StringBuilder();
            for (byte b : macData) {
                result.append(String.format("%02x", b));
            }
            return result.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error calculating HMAC", e);
        }
    }

    public boolean checkIdempotency(){
        return true ;
    }

    public void HmacValidation(Map<String, Object> payload, HttpServletRequest request){
        // step 1: Extract the HMAC from the request
        String receivedHmac = request.getParameter("hmac");
        System.out.println("Received HMAC: " + receivedHmac);
        if (receivedHmac == null) {
            throw new RuntimeException("HMAC is missing in the request");
        }
        // Step 2: Extract the required fields from the payload
        List<String> hmacKeys = Arrays.asList(
                "amount_cents",
                "created_at",
                "currency",
                "error_occured",
                "has_parent_transaction",
                "id", // obj.id
                "integration_id",
                "is_3d_secure",
                "is_auth",
                "is_capture",
                "is_refunded",
                "is_standalone_payment",
                "is_voided",
                "order.id",
                "owner",
                "pending",
                "source_data.pan",
                "source_data.sub_type",
                "source_data.type",
                "success"
        );
        // Step 3: Concatenate the values into a single string
        String concatenatedValues = concatenateValues(payload, hmacKeys);
        // Step 4: Hash the concatenated string using your HMAC secret key
        String calculatedHmac = calculateHmac(concatenatedValues, hmacSecretKey);
        System.out.println("Calculated HMAC: " + calculatedHmac);
        // Step 5: Compare the received HMAC with the calculated HMAC
        if (!receivedHmac.equals(calculatedHmac)) {
            throw new RuntimeException("Invalid HMAC signature");
        }
    }
  
    public void callbackValidation(Map<String, Object> payload, HttpServletRequest request) {
        HmacValidation(payload,  request);
        checkIdempotency();
    }

    @Transactional
    public void processPaymentCallback(Map<String, Object> payload) {
        
        String success       = (String) payload.get("success");
        //String paymobOrderId = (String) payload.get("order_id");
        if ("true".equalsIgnoreCase(success)) {
            payment.setStatus(Payment.Status.ACCEPTED);
            paymentRepository.save(payment);
        } else {
            payment.setStatus(Payment.Status.FAILED);
            paymentRepository.save(payment);    
        }

        //  Store reservation
        //  One reservation could have multiple payment trials
        //  Send payment status as email whatever the status
 

    }


}
