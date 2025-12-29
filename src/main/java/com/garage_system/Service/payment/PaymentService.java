package com.garage_system.Service.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.garage_system.DTO.request.ReservationDto;
import com.garage_system.Model.IdempotencyKey;
import com.garage_system.Model.Reservation;
import com.garage_system.Model.User;
import com.garage_system.Repository.IdempotencyKeyRepository;
import com.garage_system.Security.CustomUserDetails;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

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

    private final IdempotencyKeyRepository idempotencyKeyRepository;
    private static final long LOCK_TIMEOUT_SECONDS = 60;
    
    public String initiateCardPayment(int reservationId , UUID idempotencyKey ) {
       
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof String) { 
            return "Please log in to initiate payment.";
        }

        BigDecimal price = BigDecimal.valueOf(100);

        String token      = authenticate();
        String orderId    = createPaymentOrder(token, price , reservationId ,  idempotencyKey);
        String paymentKey = generatePaymentKey(token, orderId, price);
        return "https://accept.paymob.com/api/acceptance/iframes/"
                + iframeId + "?payment_token=" + paymentKey;
    }


    private String authenticate() {
        JSONObject body = new JSONObject();
        body.put("api_key", apiKey);
        // call : https://accept.paymob.com/api/auth/tokens
        JSONObject response = postJson(authUrl, body);
        return response.getString("token");
    }

   @Transactional
    protected String createPaymentOrder(String token, BigDecimal price, int reservationId, UUID key) {
    // 1. Idempotency Check
    IdempotencyKey record = getIdempotency(key);
    
    // If we already finished this specific request, return the cached Order ID
    if ("COMPLETED".equals(record.getStatus())) {
        return record.getResponse_body(); 
    }

    try {
        // 2. Prepare Paymob Request Body
        int amountCents = price.multiply(BigDecimal.valueOf(100)).intValue();
        
        Map<String, Object> body = Map.of(
            "auth_token", token,
            "delivery_needed", false,
            "currency", "EGP",
            "amount_cents", amountCents,
            "items", List.of(Map.of(
                "name", "Parking Slot",
                "amount_cents", amountCents,
                "description", "reservationId_" + reservationId
            ))
        );

        // 3. Call Gateway
        JSONObject response = postJson(orderUrl, new JSONObject(body));
        String orderId = String.valueOf(response.getInt("id"));

        // 4. Finalize Idempotency
        record.setStatus("COMPLETED");
        record.setResponse_body(orderId); // Store OrderID to return on retries
        record.setResponse_code(200);
        
        return orderId;

    } catch (Exception e) {
        // Rollback happens automatically; next retry will find no record or zombie
        throw new RuntimeException("Paymob Order Creation Failed", e);
    }
}


    private String generatePaymentKey(String token, String orderId, BigDecimal price) {
       
        User currentAuthUser = ((CustomUserDetails) SecurityContextHolder.getContext()
                        .getAuthentication().getPrincipal()).getUser();

       JSONObject billingData = new JSONObject();
        billingData.put("first_name", currentAuthUser.getName()); // split if you store full name
        billingData.put("last_name", "Y");
        billingData.put("email", currentAuthUser.getEmail());
        billingData.put("phone_number", currentAuthUser.getPhone());
        billingData.put("street", "User street");        // mandatory
        billingData.put("building", "Building info");    // mandatory
        billingData.put("floor", "Floor info");          // mandatory
        billingData.put("apartment", "Apartment info");  // mandatory
        billingData.put("city", "User city");
        billingData.put("country", "User country");
        billingData.put("created_at", LocalDateTime.now().toString()); // optional


       
        JSONObject body = new JSONObject();
        body.put("auth_token", token);
        body.put("order_id", orderId);
        body.put("amount_cents", price.multiply(BigDecimal.valueOf(100)).intValue());
        body.put("billing_data", billingData);
        body.put("currency", "EGP");
        body.put("integration_id", cardIntegrationId);

        //call : https://accept.paymob.com/api/acceptance/payment_keys
        JSONObject response = postJson(paymentKeyUrl, body);
        return response.getString("token");
    }


    private JSONObject postJson(String url, JSONObject body) {
            RestTemplate restTemplate = new RestTemplate();
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

    @Transactional
public IdempotencyKey getIdempotency(UUID key) {
    return idempotencyKeyRepository.findByIdWithLock(key).map(record -> {
        if ("COMPLETED".equals(record.getStatus())) return record;
        
        boolean isZombie = record.getCreatedAt().isBefore(LocalDateTime.now().minusSeconds(60));
        if (!isZombie && "PROCESSING".equals(record.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Payment already processing");
        }
        
        // Reset zombie
        record.setCreatedAt(LocalDateTime.now());
        record.setStatus("PROCESSING");
        return idempotencyKeyRepository.save(record);
    }).orElseGet(() -> {
        IdempotencyKey newRecord = new IdempotencyKey();
        newRecord.setIdempotency_key(key);
        newRecord.setStatus("PROCESSING");
        newRecord.setCreatedAt(LocalDateTime.now());
        return idempotencyKeyRepository.save(newRecord);
    });
}

}
