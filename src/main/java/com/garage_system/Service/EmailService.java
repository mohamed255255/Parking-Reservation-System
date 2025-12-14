package com.garage_system.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmailService {

    private JavaMailSender mailSender ;

    @Value("${spring.mail.username}")
    private String from ;


    public boolean sendVerificationEmail(String to , String code){
          try {
               String linkToVerification =  "http://localhost:8080/verify-user/" ;
               SimpleMailMessage message = new SimpleMailMessage();
               message.setFrom(from);
               message.setTo(to);
               message.setSubject("email creation");
               message.setText("your verification code is " + code  + " please verify you account using this link \n" + linkToVerification); /// could be html 
               mailSender.send(message);
          } catch (Exception e) {
             /// return custom exception i think 500 that email failed to send  
              return false ;
          }  
         
          return true ;
    }

    public String sendPasswordResetEmail(String email){
         return "" ;
    }
    

}
