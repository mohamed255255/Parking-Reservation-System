package com.garage_system.Service.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.garage_system.DTO.request.ReservationDto;
import com.garage_system.Model.Reservation;
import com.garage_system.Model.User;
import com.garage_system.Security.CustomUserDetails;

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


    
    public String initiateCardPayment(int reservationId) {
       
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof String) { 
            return "Please log in to initiate payment.";
        }

        BigDecimal price = BigDecimal.valueOf(100);

        String token      = authenticate();
        String orderId    = createPaymentOrder(token, price , reservationId);
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


    private String createPaymentOrder(String token, BigDecimal price , int reservationId) {
        int amount_cents = price.multiply(BigDecimal.valueOf(100)).intValue() ;

        JSONObject body = new JSONObject();
        body.put("auth_token", token);
        body.put("delivery_needed", false);
        body.put("currency", "EGP");
        body.put("amount_cents", amount_cents );
        
        JSONArray items = new JSONArray();       
        JSONObject oneItem = new JSONObject();
        oneItem.put("name", "Parking Slot");
        oneItem.put("amount_cents", amount_cents);
        oneItem.put("description", "reservationId_"+reservationId);
        items.put(oneItem);
       
        body.put("items", items);

        
        // ORDER_URL = https://accept.paymob.com/api/ecommerce/orders
        JSONObject response = postJson(orderUrl, body);
        return String.valueOf(response.getInt("id"));
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

}
