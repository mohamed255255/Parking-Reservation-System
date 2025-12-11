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


    public boolean sendVerificationEmail(String to , String token){
          try {
               String linkToVerification =  "http://localhost:8080/verify-user/"  + token ;
               SimpleMailMessage message = new SimpleMailMessage();
               message.setFrom(from);
               message.setTo(to);
               message.setSubject("email creation");
               message.setText("hi please create ur account using this link \n" + linkToVerification); /// could be html 
               mailSender.send(message);
        

          } catch (Exception e) {
               
              return false ;
          }  
         
          return true ;
    }

    public String sendPasswordResetEmail(String email){
         return "" ;
    }
    

}
