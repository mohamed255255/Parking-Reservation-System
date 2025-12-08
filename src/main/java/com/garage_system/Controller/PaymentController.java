package com.garage_system.Controller ;
import lombok.RequiredArgsConstructor;

import javax.swing.text.html.Option;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.jsonwebtoken.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.garage_system.Model.User;
import com.garage_system.Security.CustomUserDetails;
import com.garage_system.Service.CustomUserDetailsService;
import com.garage_system.Service.JWTService;
import com.garage_system.Service.payment.PaymentService;

@RestController
@RequestMapping("/api/payments")
@PreAuthorize("hasAnyRole('USER')")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;


    @PostMapping("/card")
    public ResponseEntity<String> initiateCardPayment() {
        String paymentLink = paymentService.initiateCardPayment();
        return ResponseEntity.ok(paymentLink);
    }

}
