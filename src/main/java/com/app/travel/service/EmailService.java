package com.app.travel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    @Value("${app.frontend.url:http://localhost:3000}")
    private String frontendUrl;

    public void sendPasswordRecovery(String email, String token) {
        // Service email désactivé pour le développement
        logger.info("=== EMAIL SERVICE (MODE DÉVELOPPEMENT) ===");
        logger.info("Email de récupération de mot de passe pour : {}", email);
        logger.info("Token : {}", token);
        logger.info("Lien de réinitialisation : {}/reset-password?token={}&email={}", frontendUrl, token, email);
        logger.info("==========================================");
        
    }
}
