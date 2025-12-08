package com.garage_system.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.garage_system.Model.Payment;
import com.garage_system.Model.Reservation;
import com.garage_system.Repository.PaymentRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentService {

    @Value("${PAYMOB.IFRAME_ID}")
    private String iframeId;

    @Value("${PAYMOB.API_KEY}")
    private String PAYMOB_API_KEY;

    @Value("${PAYMOB.AUTH_URL}")
    private String PAYMOB_AUTH_URL;

    @Value("${PAYMOB.ORDER_URL}")
    private String PAYMOB_ORDER_URL;

    @Value("${PAYMOB.PAYMENT_KEY_URL}")
    private String PAYMOB_PAYMENT_KEY_URL;

    @Value("${PAYMOB.CARD_INTEGRATION_ID}")
    private int cardIntegrationId;

    @Value("${PAYMOB.WALLET_INTEGRATION_ID}")
    private int walletIntegrationId;

    @Value("${PAYMOB.HMAC_SECRET}")
    private String hmacSecretKey;

    private final PaymentRepository paymentRepository;
    private final ProcessingService processingService;

    private static final ObjectMapper mapper = new ObjectMapper();

    public static class PaymentResponse {
        public String paymentURL;
        public PaymentResponse(String url) { this.paymentURL = url; }
    }

    //------------------------------------
    // CARD PAYMENT
    //------------------------------------
@Transactional
public Map<String, Object> initiateCardPayment() {
    try {
        Payment payment = createPendingPayment(Payment.Method.PAYMOB_CARD);

        String authToken = getAuthToken();
        String orderId = createPaymobOrder(authToken, payment);
        String paymentKey = generatePaymentKey(authToken, orderId, payment.getAmount(), cardIntegrationId);

        String iframeUrl = generatePaymentIframeUrl(paymentKey);

        return Map.of(
                "payment_id", payment.getPaymentId(),
                "iframe_url", iframeUrl,
                "status", "PENDING"
        );

    } catch (Exception e) {
        throw new RuntimeException("Card payment error: " + e.getMessage(), e);
    }
}
private Payment createPendingPayment(Payment.Method method) {
    Payment payment = new Payment();
    payment.setAmount(100.0); // TODO replace
    payment.setStatus(Payment.Status.PENDING);
    payment.setMethod(method);
    return paymentRepository.save(payment);
}


/* 
    //------------------------------------
    // WALLET PAYMENT
    //------------------------------------
    @Transactional
    public PaymentResponse initiateWalletPayment(Reservation reservation, String walletNumber) {
        try {
            Payment payment = new Payment();
            payment.setReservation(reservation);
            payment.setAmount(100.0);          // TODO: replace later
            payment.setStatus(Payment.Status.PENDING);
            payment.setMethod(Payment.Method.PAYMOB_E_WALLET);

            paymentRepository.save(payment);

            String authToken = getAuthToken();
            String orderId = createPaymobOrder(authToken, payment);
            String token = generatePaymentKey(authToken, orderId, payment.getAmount(), walletIntegrationId);

            String url = payUsingWallet(token, walletNumber);
            return new PaymentResponse(url);

        } catch (Exception e) {
            throw new RuntimeException("Wallet payment error: " + e.getMessage(), e);
        }
    }
*/
    //------------------------------------
    // CALLBACK
    //------------------------------------
    public void handlePaymentCallback(Map<String, Object> payload, HttpServletRequest request) {
        try {
            String receivedHmac = request.getParameter("hmac");
            if (receivedHmac == null) {
                throw new RuntimeException("HMAC missing");
            }

            List<String> hmacKeys = Arrays.asList(
                    "amount_cents", "created_at", "currency", "error_occured",
                    "has_parent_transaction", "id", "integration_id", "is_3d_secure",
                    "is_auth", "is_capture", "is_refunded", "is_standalone_payment",
                    "is_voided", "order.id", "owner", "pending",
                    "source_data.pan", "source_data.sub_type", "source_data.type", "success"
            );

            String concatenated = concatenateValues(payload, hmacKeys);
            String expectedHmac = calculateHmac(concatenated, hmacSecretKey);

            if (!receivedHmac.equals(expectedHmac)) {
                throw new RuntimeException("Invalid HMAC");
            }

            processingService.processPaymentCallback(payload);

        } catch (Exception e) {
            throw new RuntimeException("Callback error: " + e.getMessage(), e);
        }
    }

    //------------------------------------
    // INTERNAL HELPERS
    //------------------------------------
    private String getAuthToken() throws Exception {
        RestTemplate rest = new RestTemplate();

        Map<String, Object> body = Map.of("api_key", PAYMOB_API_KEY);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> res = rest.exchange(
                PAYMOB_AUTH_URL,
                HttpMethod.POST,
                new HttpEntity<>(mapper.writeValueAsString(body), headers),
                String.class
        );

        Map<String, Object> json = mapper.readValue(res.getBody(), new TypeReference<>() {});
        return json.get("token").toString();
    }

    private String createPaymobOrder(String token, Payment payment) throws Exception {
        RestTemplate rest = new RestTemplate();

        Map<String, Object> body = new HashMap<>();
        body.put("auth_token", token);
        body.put("delivery_needed", false);
        body.put("amount_cents", (int) (payment.getAmount() * 100));
        body.put("currency", "EGP");
        body.put("items", List.of());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> res = rest.exchange(
                PAYMOB_ORDER_URL,
                HttpMethod.POST,
                new HttpEntity<>(mapper.writeValueAsString(body), headers),
                String.class
        );

        Map<String, Object> json = mapper.readValue(res.getBody(), new TypeReference<>() {});
        String orderId = json.get("id").toString();

        payment.setPaymentId(orderId);
        paymentRepository.save(payment);

        return orderId;
    }

    private String generatePaymentKey(
            String token,
            String orderId,
            double amount,
            int integrationId
    ) throws Exception {

        RestTemplate rest = new RestTemplate();

        Map<String, Object> billing = new HashMap<>();
        billing.put("email", "user@example.com");
        billing.put("first_name", "John");
        billing.put("last_name", "Doe");
        billing.put("phone_number", "0123456789");

        Map<String, Object> body = new HashMap<>();
        body.put("auth_token", token);
        body.put("amount_cents", (int) (amount * 100));
        body.put("expiration", 3600);
        body.put("order_id", orderId);
        body.put("billing_data", billing);
        body.put("currency", "EGP");
        body.put("integration_id", integrationId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> res = rest.exchange(
                PAYMOB_PAYMENT_KEY_URL,
                HttpMethod.POST,
                new HttpEntity<>(mapper.writeValueAsString(body), headers),
                String.class
        );

        Map<String, Object> json = mapper.readValue(res.getBody(), new TypeReference<>() {});
        return json.get("token").toString();
    }

    private String generatePaymentIframeUrl(String token) {
        return "https://accept.paymob.com/api/acceptance/iframes/" + iframeId + "?payment_token=" + token;
    }

    /*private String payUsingWallet(String token, String walletNumber) throws Exception {
        RestTemplate rest = new RestTemplate();

        Map<String, Object> source = Map.of(
                "identifier", walletNumber,
                "subtype", "WALLET"
        );

        Map<String, Object> body = Map.of(
                "source", source,
                "payment_token", token
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> res = rest.exchange(
                "https://accept.paymob.com/api/acceptance/payments/pay",
                HttpMethod.POST,
                new HttpEntity<>(mapper.writeValueAsString(body), headers),
                String.class
        );

        Map<String, Object> json = mapper.readValue(res.getBody(), new TypeReference<>() {});
        return json.get("redirect_url").toString();
    }*/

    private String concatenateValues(Map<String, Object> payload, List<String> keys) {
        Map<String, Object> obj = (Map<String, Object>) payload.get("obj");

        StringBuilder out = new StringBuilder();

        for (String k : keys) {
            String[] parts = k.split("\\.");
            Object val = null;

            if (parts.length == 1) {
                val = obj.get(parts[0]);
            } else if (parts.length == 2) {
                Object nested = obj.get(parts[0]);
                if (nested instanceof Map) {
                    val = ((Map<?, ?>) nested).get(parts[1]);
                }
            }

            out.append(val != null ? val.toString() : "");
        }

        return out.toString();
    }

    private String calculateHmac(String data, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA512");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA512"));

            byte[] result = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

            StringBuilder hex = new StringBuilder();
            for (byte b : result) hex.append(String.format("%02x", b));

            return hex.toString();
        } catch (Exception e) {
            throw new RuntimeException("HMAC error", e);
        }
    }
}
