package com.parking_reservation_system.service.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.parking_reservation_system.dto.request.ReservationDto;
import com.parking_reservation_system.model.IdempotencyKey;
import com.parking_reservation_system.model.Reservation;
import com.parking_reservation_system.model.User;
import com.parking_reservation_system.repository.IdempotencyKeyRepository;
import com.parking_reservation_system.repository.ReservationRepository;
import com.parking_reservation_system.security.CustomUserDetails;

import jakarta.persistence.LockModeType;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
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
    private final ReservationRepository reservationRepository ;

    public PaymentService(ReservationRepository reservationRepository , IdempotencyKeyRepository idempotencyKeyRepository){
       this.idempotencyKeyRepository = idempotencyKeyRepository ;
       this.reservationRepository = reservationRepository ;
    }

    /*
    after I added @transactional at the initiatecardPayment (parent)
    I prevented the error 
    [ERROR: cannot execute SELECT FOR NO KEY UPDATE in a read-only transaction]
    since the children have Transactional but the parent dont
    # Some databases (like Postgres) still allow SELECT, 
    but any write or lock that requires a transaction fails.
    */
    @Transactional
    public String initiateCardPayment(int reservationId , UUID idempotencyKey ) {
      
        // Security/Logic Check: Ensure reservation exists before doing anything
        reservationRepository.findById(reservationId).orElseThrow(() -> new RuntimeException("CRITICAL: Reservation " + reservationId + " is not found"));
         Optional<IdempotencyKey> recordOpt = getIdempotencyKey(idempotencyKey);
         if (recordOpt.isPresent()) {
            IdempotencyKey record = recordOpt.get(); 
            if ("COMPLETED".equals(record.getStatus())) 
              return record.getResponse_body();
            
        }else{
          //// transaction is still active's lock still active too
           IdempotencyKey newRecord = new IdempotencyKey();
           newRecord.setIdempotency_key(idempotencyKey);
           newRecord.setStatus("PROCESSING");
           newRecord.setPayload("{}");
           newRecord.setCreatedAt(LocalDateTime.now());
           idempotencyKeyRepository.save(newRecord); 
        };
        
        BigDecimal price = BigDecimal.valueOf(100);
        String token      = authenticate();
        String orderId    = createPaymentOrder(token, price , reservationId ,  idempotencyKey);
        System.out.println("============================ order id  : " + orderId +"=======================================");
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

    public Optional<IdempotencyKey> getIdempotencyKey(UUID key) {
        return idempotencyKeyRepository.findById(key).map(
            record -> {
               if ("COMPLETED".equals(record.getStatus())) return record;
            
                boolean isZombie = record.getCreatedAt().isBefore(LocalDateTime.now().minusSeconds(60));
                if (!isZombie && "PROCESSING".equals(record.getStatus())) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Payment already processing");
                }
            
                // Reset zombie
                record.setCreatedAt(LocalDateTime.now());
                record.setStatus("PROCESSING");
                return idempotencyKeyRepository.save(record);

        });
    }


    @Transactional
    protected String createPaymentOrder(String token,BigDecimal price,int reservationId, UUID key){
    try {
        int amountCents =
                price.multiply(BigDecimal.valueOf(100)).intValueExact();

        JSONObject body = new JSONObject();
        body.put("auth_token", token);
        body.put("delivery_needed", false);
        body.put("currency", "EGP");
        body.put("amount_cents", amountCents);
    
        JSONArray items    = new JSONArray();       
        JSONObject oneItem = new JSONObject();
        oneItem.put("name", "Parking Slot");
        oneItem.put("amount_cents", amountCents);
        oneItem.put("description", "reservationId_"+reservationId+","+"Idempotencykey_"+key);
        items.put(oneItem);
       
        body.put("items", items);

        JSONObject apiResponse = postJson(orderUrl, body);
    
        String orderId =  String.valueOf(apiResponse.getInt("id"));
  
        return orderId ;

    } catch (Exception e) {
        throw new RuntimeException(e.getMessage(), e);
    }
}


    private String generatePaymentKey(String token, String orderId, BigDecimal price) {
        
        User currentAuthUser = ((CustomUserDetails) SecurityContextHolder.getContext()
                        .getAuthentication().getPrincipal()).getUser();

       JSONObject billingData = new JSONObject();
        billingData.put("first_name", currentAuthUser.getName()); // split if you store full name
        billingData.put("last_name", " LastName");
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
