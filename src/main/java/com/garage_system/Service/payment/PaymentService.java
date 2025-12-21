package com.garage_system.Service.payment;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.garage_system.Model.User;
import com.garage_system.Security.CustomUserDetails;

import org.json.JSONObject;
import org.json.JSONArray;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

@Service
@RequiredArgsConstructor

public class PaymentService {

    @Value("${PAYMOB.API_KEY}")
    private String apiKey;

    @Value("${PAYMOB.AUTH_URL}")
    private String authUrl;

    @Value("${PAYMOB.ORDER_URL}")
    private String orderUrl;

    @Value("${PAYMOB.PAYMENT_KEY_URL}")
    private String paymentKeyUrl;

    @Value("${PAYMOB.IFRAME_ID}")
    private String iframeId;

    @Value("${PAYMOB.CARD_INTEGRATION_ID}")
    private int cardIntegrationId;

    @Value("${PAYMOB.HMAC_SECRET}")
    private String hmacSecretKey;

    private final RestTemplate restTemplate = new RestTemplate();

    private  final WebhookService webhookService ;

    public String initiateCardPayment() {
       
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof String) { // usually a string "anonymousUser" if not logged in
            return "Please log in to initiate a payment.";
        }

        BigDecimal price = BigDecimal.valueOf(100);

        String token      = authenticate();
        String orderId    = createPaymentOrder(token, price);
        String paymentKey = generatePaymentKey(token, orderId, price);
        return "https://accept.paymob.com/api/acceptance/iframes/"
                + iframeId + "?payment_token=" + paymentKey;
    }


    private String authenticate() {
        JSONObject body = new JSONObject();
        body.put("api_key", apiKey);

        JSONObject res = postJson(authUrl, body);
        return res.getString("token");
    }


    private String createPaymentOrder(String token, BigDecimal price) {
        JSONObject body = new JSONObject();
        body.put("auth_token", token);
        body.put("delivery_needed", false);
        body.put("currency", "EGP");
        body.put("amount_cents", price.multiply(BigDecimal.valueOf(100)).intValue());
        body.put("items", new JSONArray());

        JSONObject res = postJson(orderUrl, body);
        return String.valueOf(res.getInt("id"));
    }


    private String generatePaymentKey(String token, String orderId, BigDecimal price) {
       
        JSONObject billingData = new JSONObject();
        billingData.put("email", "user@example.com");
        billingData.put("first_name", "John");
        billingData.put("last_name", "Doe");
        billingData.put("phone_number", "+201234567890");

        billingData.put("street", "Test Street");
        billingData.put("building", "123");
        billingData.put("floor", "1");
        billingData.put("apartment", "1");
        billingData.put("city", "Cairo");
        billingData.put("country", "EG");

       
        JSONObject body = new JSONObject();
        body.put("auth_token", token);
        body.put("order_id", orderId);
        body.put("amount_cents", price.multiply(BigDecimal.valueOf(100)).intValue());
        body.put("billing_data", billingData);
        body.put("currency", "EGP");
        body.put("integration_id", cardIntegrationId);

        JSONObject res = postJson(paymentKeyUrl, body);
        return res.getString("token");
    }


    private JSONObject postJson(String url, JSONObject body) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);

            try {
                ResponseEntity<String> response = restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        entity,
                        String.class
                );
                return new JSONObject(response.getBody());
            } catch (HttpClientErrorException e) {
                throw new RuntimeException("Paymob Error: " + e.getResponseBodyAsString());
            }
        }


////////////////////////////////////Explain
/// 
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



        public void handlePaymentCallback(Map<String, Object> payload, HttpServletRequest request) {
            // TODO: Implement callback handling logic
            // This method should verify the HMAC, update payment status, and perform any post-payment actions
            // Step 1: Extract the HMAC from the request
          /* */  String receivedHmac = request.getParameter("hmac");
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
    
            // Step 6: Proceed with the logic (e.g., enroll the student)
            webhookService.processPaymentCallback(payload);
        }
}
