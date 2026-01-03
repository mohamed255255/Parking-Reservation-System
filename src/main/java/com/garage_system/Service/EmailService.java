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

    public void sendMail(String to , String subject , String body){
          try {
               SimpleMailMessage message = new SimpleMailMessage();
               message.setFrom(from);
               message.setTo(to);
               message.setSubject(subject);
               message.setText(body);
               mailSender.send(message);
          } catch (Exception e) {
                    throw new RuntimeException("Failed to send email");
          }
     }
               
    public boolean sendVerificationEmail(String to , String code){
          /// should be thymleaf to either wirte the code or just open a dummy page
          String linkToVerification =  "frontendUrl/verify-email?email="+to ;
          String subject = "email verification";
          String body = "your verification code is : " + code  + "\nplease verify you account using this link : \n " + linkToVerification;
          sendMail(to, subject, body);
          return true ;
    }

    public String sendPasswordResetEmail(String to , String token){
          String linkToVerification =  "frontendUrl/reset-password?token="+token ;             
          String subject = "password reset";
          String body = "to reset your password visit this link : \n " + linkToVerification ;
          sendMail(to, subject, body);
          return "Success"; /////////////////// to do : return better response ;
    }
    

}
