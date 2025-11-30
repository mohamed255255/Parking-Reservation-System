package com.garage_system.Service;

import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

@Service
public class PaypalService {
    @Autowired
    private APIContext apiContext ;

    // payment method

    public Payment createPayment(Double total , String Currency , String method 
        , String intent , String description , String cancelUrl , String successUrl){

            Amount amount = new Amount();
            amount.setCurrency(Currency);
            amount.setTotal(String.format("%.2f" , total));

            //A transaction
            Transaction transaction = new Transaction();
            transaction.setDescription(description);
            transaction.setAmount(amount);

            List<Transaction> groupOfTransactions = new ArrayList<Transaction>();
            groupOfTransactions.add(transaction);


            /// payer  
            Payer payer = new Payer();
            payer.setPaymentMethod(method.toUpperCase());
            
            
            //payment obj
            Payment payment = new Payment();
            payment.setIntent(intent);
            payment.setPayer(payer);
            payment.setTransactions(groupOfTransactions);

            ///Success and cancel URLs
            RedirectUrls RedirectUrls = new RedirectUrls();
            RedirectUrls.setCancelUrl(cancelUrl);
            RedirectUrls.setReturnUrl(successUrl);
            
            payment.setRedirectUrls(RedirectUrls);


            return payment ;
    }


    /// execute payment 
     public Payment execute(String ppaymentId , String payerId) throws PayPalRESTException{
        Payment payment = new Payment();
        payment.setId(ppaymentId);
      
      
        PaymentExecution PaymentExecution = new PaymentExecution();
        PaymentExecution.setPayerId(payerId);
      
        return payment.execute(apiContext, PaymentExecution);

     }

     
}
