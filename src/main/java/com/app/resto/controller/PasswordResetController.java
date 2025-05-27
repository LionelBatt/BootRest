package com.app.resto.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.resto.model.Users;
import com.app.resto.service.PasswordResetService;

@RestController
@RequestMapping("/api/password-recovery")
public class PasswordResetController {
	
	@Autowired
	PasswordResetService service;

	@PostMapping("/forgot")
	public ResponseEntity<String> forgotPassword(@RequestBody Users request) {
		service.forgotPassword( request.getEmail());
		return ResponseEntity.ok("Password reset link has been sent to your email.");
	}

	@GetMapping("/reset")
	public ResponseEntity<String> resetPassword(@RequestParam String email, @RequestParam String token) {
		if (service.validateToken(email, token).isPresent()) {
			return ResponseEntity.ok("Formulaire de réinitialisation de mot de passe"); 
		} else {
			return ResponseEntity.badRequest().body("Token invalide ou expiré.");
		}
	}

	@PostMapping("/change")
	public ResponseEntity<String> changePassword(@RequestBody Map<String, String> request) {
		String email = request.get("email");
		String token = request.get("token");
		String newPassword = request.get("newPassword");
		service.changePassword(email, token, newPassword);
		return ResponseEntity.ok("Mot de passe réinitialisé avec succès");
	}
}
