package com.app.travel.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.travel.dto.ApiResponse;
import com.app.travel.model.Users;
import com.app.travel.service.PasswordResetService;

@RestController
@RequestMapping("/api/password-recovery")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PasswordResetController {
	
	@Autowired
	PasswordResetService service;

	@PostMapping("/forgot")
	public ApiResponse<String> forgotPassword(@RequestBody Users request) {
		try {
			service.forgotPassword(request.getEmail());
			return ApiResponse.success("Email envoyé", "Le lien de réinitialisation a été envoyé à votre email");
		} catch (Exception e) {
			return ApiResponse.error("Erreur lors de l'envoi", "Impossible d'envoyer l'email de réinitialisation");
		}
	}

	@GetMapping("/reset")
	public ApiResponse<String> resetPassword(@RequestParam String email, @RequestParam String token) {
		if (service.validateToken(email, token).isPresent()) {
			return ApiResponse.success("Token valide", "Formulaire de réinitialisation de mot de passe");
		} else {
			return ApiResponse.error("Token invalide", "Token invalide ou expiré");
		}
	}

	@PostMapping("/change")
	public ApiResponse<String> changePassword(@RequestBody Map<String, String> request) {
		try {
			String email = request.get("email");
			String token = request.get("token");
			String newPassword = request.get("newPassword");
			service.changePassword(email, token, newPassword);
			return ApiResponse.success("Mot de passe modifié", "Mot de passe réinitialisé avec succès");
		} catch (Exception e) {
			return ApiResponse.error("Erreur de réinitialisation", "Impossible de réinitialiser le mot de passe");
		}
	}
}
