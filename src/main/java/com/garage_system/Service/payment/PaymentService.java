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

import java.math.BigDecimal;
import java.util.Map;
import java.util.logging.Logger;

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

    private final RestTemplate restTemplate = new RestTemplate();


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
        JSONObject body = new JSONObject();
        body.put("auth_token", token);
        body.put("order_id", orderId);
        body.put("amount_cents", price.multiply(BigDecimal.valueOf(100)).intValue());
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
}
