package com.garage_system.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    private APIContext apiContext ;

    public PaypalService(APIContext apiContext){
        this.apiContext = apiContext ;
    }
    
    // payment method
    public Payment createPayment(Double total , String currency , String method,
        String intent , String description , String cancelUrl , String successUrl) throws PayPalRESTException {

        Amount amount = new Amount();
        amount.setCurrency(currency);
        // Handle diff currency format and decimal point : 9.99 USD or 9,99 EUR
        amount.setTotal(String.format(Locale.forLanguageTag(currency) , "%.2f" , total)); 

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(method.toUpperCase()); // method : creditcard or paypal

        /// final payment: intent , method and group of transactions (contain amounts inside each one) made
        Payment payment = new Payment();
        payment.setIntent(intent);
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
      
        payment.setRedirectUrls(redirectUrls);
        return payment.create(apiContext);
    }



    /// execute payment 
     public Payment execute(String paymentId , String payerId) throws PayPalRESTException{
       
        Payment payment = new Payment();
        payment.setId(paymentId);
      
        PaymentExecution PaymentExecution = new PaymentExecution();
        PaymentExecution.setPayerId(payerId);
      
        return payment.execute(apiContext, PaymentExecution);

     }

     
}
