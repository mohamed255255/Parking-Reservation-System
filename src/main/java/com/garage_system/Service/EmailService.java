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


    public void sendAccountCreationEmail(String to , String token){
         String link = "URL for the register" + token ;

         SimpleMailMessage message = new SimpleMailMessage();
         message.setFrom(link);
         message.setFrom(link);
         message.setTo(from);
         message.setSubject(to);
         message.setText("hi please create ur account using this link \n" + link); /// could be html 
         mailSender.send(message);

    }
    public String sendPasswordResetEmail(String email){
         return "" ;
    }
    

}
