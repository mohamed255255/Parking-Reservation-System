package com.garage_system.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.garage_system.Service.PaypalService;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;


@RestController
@RequestMapping("/paypal")
public class PayPalController {
     
    private final PaypalService paypalService ;

    public PayPalController(PaypalService paypalService){
        this.paypalService = paypalService ;
    }

    @PostMapping("/pay")
    public String makePayment(@RequestParam double amount) throws PayPalRESTException {
        Payment payment = paypalService.createPayment(
            amount,
            "USD",
            "paypal",
            "sale",
            "Payment for parking",
            "http://localhost:8080/paypal/cancel",
            "http://localhost:8080/paypal/success"
        );

        return payment.getLinks()
                .stream()
                .filter(link -> link.getRel().equals("approval_url"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No approval URL returned"))
                .getHref();
    }


    @GetMapping("/success")  
    public String success(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) throws PayPalRESTException{
        Payment payment = paypalService.execute(paymentId, payerId);

        if (payment.getState().equals("approved")) {
            return "Payment completed successfully.";
        }
        return "Payment failed.";
    }




    @GetMapping("/cancel")
    public String cancel() {
        return "Payment was cancelled.";
    }


  
    
}
