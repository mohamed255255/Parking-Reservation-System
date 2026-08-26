package com.parking_reservation_system.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
        
    }

    public void sendVerificationEmail(String to, String code) {
        String linkToVerification = "frontendUrl/verify-email";
        String subject = "email verification";
        String body =
                "your verification code is : "
                        + code
                        + "\nplease verify your account using this link : \n "
                        + linkToVerification;
        sendMail(to, subject, body);
    }

    public void sendPasswordResetEmail(String to, String token) {
        String linkToVerification = "frontendUrl/reset-password" ;
        String subject = "password reset";
        String body = "to reset your password visit this link : \n " + linkToVerification;
        sendMail(to, subject, body);
    }
}
