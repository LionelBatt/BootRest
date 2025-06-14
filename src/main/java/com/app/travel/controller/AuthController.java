package com.app.travel.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.travel.dto.ApiResponse;
import com.app.travel.model.Role;
import com.app.travel.model.Users;
import com.app.travel.repos.UserRepository;
import com.app.travel.security.JwtUtil;
import com.app.travel.service.LoginAttemptService;
import com.app.travel.service.TokenBlacklistService;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserRepository repos;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private JwtUtil jwtUtils;
	
	@Autowired
	private LoginAttemptService loginAttemptService;

	@Autowired
	private TokenBlacklistService tokenBlacklistService;

	@PostMapping("/signin")
	public ApiResponse<String> authenticateUser(@RequestBody Users user) {
		String username = user.getUsername();

		// Vérifier si le compte est verrouillé
		if (loginAttemptService.isBlocked(username)) {
			LocalDateTime unlockTime = loginAttemptService.getUnlockTime(username);
			if (unlockTime != null) {
				String unlockTimeStr = unlockTime.format(DateTimeFormatter.ofPattern("HH:mm"));
				return ApiResponse.error("Compte verrouillé", "Trop de tentatives. Réessayez après " + unlockTimeStr);
			}
		}	
		try {
			// Authentifier l'utilisateur
			Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, user.getPassword()));
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			String token = jwtUtils.generateToken(userDetails.getUsername());
			
			// Connexion réussie - réinitialiser les tentatives
			loginAttemptService.loginSucceeded(username);
			return ApiResponse.success("Connexion réussie", token);
			
		} catch (Exception e) {

			loginAttemptService.loginFailed(username);
			int remaining = loginAttemptService.getRemainingAttempts(username);
			String message = "Nom d'utilisateur ou mot de passe incorrect";

			if (remaining > 0) {
				message += " (Tentatives restantes: " + remaining + ")";
			}
			
			return ApiResponse.error("Échec de l'authentification", message);
		}
	}
	
    @PostMapping("/signup")
	public ApiResponse<String> registerUser(@RequestBody Users user) {
		if (repos.existsByUsername(user.getUsername())) {
			return ApiResponse.error("Erreur", "Le nom d'utilisateur est déjà pris!");
		}
		try {
			Users newUser = new Users(
				user.getUsername(), 
				encoder.encode(user.getPassword()), 
				user.getEmail(),
				user.getPhoneNumber(),
				user.getName(),
				user.getSurname(), 
				user.getAddress()
			);
			newUser.setRole(Role.USER);
			repos.save(newUser);
			return ApiResponse.success("Utilisateur enregistré avec succès!", "Compte créé");
		} catch (Exception e) {
			return ApiResponse.error("Erreur lors de l'inscription", "Impossible de créer le compte");
		}
	}

    @PostMapping("/signout")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<?> signoutUser(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String jwt = authHeader.substring(7);
			tokenBlacklistService.blacklistToken(jwt);
			return ResponseEntity.ok(ApiResponse.success("Déconnexion réussie", "Vous avez été déconnecté avec succès"));
		}

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("Déconnexion échouée", "Token manquant ou invalide"));
	}
}