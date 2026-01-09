package com.parking_reservation_system.service.payment;


import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.parking_reservation_system.model.Payment;
import com.parking_reservation_system.model.Reservation;
import com.parking_reservation_system.model.Payment.Method;
import com.parking_reservation_system.model.Payment.Status;
import com.parking_reservation_system.repository.PaymentRepository;
import com.parking_reservation_system.repository.ReservationRepository;
import com.parking_reservation_system.service.EmailService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor ;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class WebhookService{

    @Value("${PAYMOB.HMAC_SECRET}")
    private String hmacSecretKey;

    private static Logger logger = LoggerFactory.getLogger(WebhookService.class);
    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository ;
    private final EmailService emailService ;

 

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
    
@Transactional
public void processPaymentCallback(Map<String, Object> payload) {
   
    logger.info("Processing Payment: {}", payload);

    Map<String, Object> obj = (Map<String, Object>) payload.get("obj");
    Map<String, Object> order = (Map<String, Object>) obj.get("order");
    List<Map<String, Object>> items = (List<Map<String, Object>>) order.get("items");
    
    // 1. Extract Reservation & Transaction Info
    Map<String, Object> item = items.get(0);
    String desc = item.get("description").toString();
    int reservationId = Integer.parseInt(desc.substring(desc.lastIndexOf('_') + 1));
    
    // Security/Logic Check: Ensure reservation exists before doing anything
    Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new RuntimeException("CRITICAL: Reservation " + reservationId + " not found"));

    // 2. Build Payment Entity
    Payment payment = new Payment();
    payment.setReservation(reservation);
    payment.setTransaction_id(obj.get("id").toString());
    payment.setAmount(Integer.parseInt(obj.get("amount_cents").toString()));

    // Map Method (Simplified check)
    String subType = String.valueOf(((Map)obj.get("source_data")).get("sub_type")).toUpperCase();
    payment.setMethod(subType.matches(".*(VISA|MASTERCARD).*") ? Method.CARD : Method.E_WALLET);

    // Map Status (Directly from payload booleans)
    if (Boolean.parseBoolean(obj.get("pending").toString())) payment.setStatus(Status.PENDING);
    else if (Boolean.parseBoolean(obj.get("is_refunded").toString())) payment.setStatus(Status.REFUNDED);
    else if (Boolean.parseBoolean(obj.get("success").toString())) {
        payment.setStatus(Status.ACCEPTED);
        reservation.setStatus(Reservation.Status.COMPLETED); // Only complete if successful
    } else {
        payment.setStatus(Status.FAILED);
        reservation.setStatus(Reservation.Status.FAILED); // Only complete if successful

    }

    // 3. Persist and Notify
    reservationRepository.save(reservation);
    paymentRepository.save(payment);
  
    // 1. Extract email from the nested shipping_data
    Map<String, Object> shippingData = (Map<String, Object>) order.get("shipping_data");
    String to = shippingData.get("email").toString();

    // 2. Define the subject
    String subject = "Payment Receipt";

    // 3. Build the body using String.format for readability
    String mailBody = String.format(
        "Your payment id : %s for reservation #%d is %s.",
        payment.getPaymentId(), 
        reservationId, 
        payment.getStatus().toString().toLowerCase()
    );

    // 4. Send payment details as notification 
    emailService.sendMail(to, subject, mailBody);
}

       
 


}
