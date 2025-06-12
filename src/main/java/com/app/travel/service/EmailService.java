package com.app.travel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordRecovery(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("${spring.mail.username}");
        message.setTo(email);
        message.setSubject("Reset your Password");
        message.setText("To reset your password, please click the link below.\n\n"
                + "http://Projet-restaurant-env.eba-gmijcpva.eu-west-3.elasticbeanstalk.com/api/reset-password?token=" + token + "&email=" + email);
        mailSender.send(message);
    }
}
