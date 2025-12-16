package com.garage_system.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    private JavaMailSender mailSender ;

    @Value("${spring.mail.username}")
    private String from ;

    public EmailService(JavaMailSender mailSender){
           this.mailSender = mailSender ;
    }


    public boolean sendVerificationEmail(String to , String code){
          try {
               /// should be thymleaf to either wirte the code or just open a dummy page
               String linkToVerification =  "http://localhost:8081/verify-user" ;
               SimpleMailMessage message = new SimpleMailMessage();
               message.setFrom(from);
               message.setTo(to);
               message.setSubject("email verification");
               message.setText("your verification code is : " + code  + "\nplease verify you account using this link : \n " + linkToVerification); /// could be html 
               mailSender.send(message);
          } catch (Exception e) {
               throw new RuntimeException("Failed to send email");
          }  
         
          return true ;
    }

    public String sendPasswordResetEmail(String email , String token){
            try {
          
               String linkToVerification =  "http://localhost:8081/reset-password?token="+token ;
               SimpleMailMessage message = new SimpleMailMessage();
               message.setFrom(from);
               message.setTo(email);
               message.setSubject("password reset");
               message.setText("to reset your password visit this link : \n " + linkToVerification); /// could be html 
               mailSender.send(message);
          } catch (Exception e) {
               throw new RuntimeException("Failed to send email");
          } 
          return "Success"; /////////////////// to do : return better response ;
    }
    

}
