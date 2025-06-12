package com.app.travel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    @Value("${app.frontend.url:http://localhost:3000}")
    private String frontendUrl;

    public void sendPasswordRecovery(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("Réinitialisation de votre mot de passe - Travel Agency");
        message.setText("Bonjour,\n\n"
                + "Vous avez demandé la réinitialisation de votre mot de passe.\n\n"
                + "Cliquez sur le lien ci-dessous pour créer un nouveau mot de passe :\n"
                + frontendUrl + "/reset-password?token=" + token + "&email=" + email + "\n\n"
                + "Ce lien expirera dans 1 heure.\n\n"
                + "Si vous n'avez pas demandé cette réinitialisation, ignorez ce message.\n\n"
                + "L'équipe Travel Agency");
        mailSender.send(message);
    }
}
