package com.garage_system.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.garage_system.Service.PaypalService;
import com.paypal.api.payments.Payment;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/paypal")
public class PayPalController {
     
    private final PaypalService paypalService ;

    public PayPalController(PaypalService paypalService){
        this.paypalService = paypalService ;
    }

    @PostMapping("/pay")
    public String makePayment(@RequestParam double amount) {
        Payment payment = paypalService.createPayment(
            500, "USDz", "paypal",
             "payment for parking", "my paypal test description", "", "");        
        return entity;
    }
    
}
