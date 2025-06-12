package com.app.travel.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.app.travel.model.Users;
import com.app.travel.repos.UserRepository;
import com.app.travel.security.JwtUtil;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UserRepository repos;
	
	@Autowired
	PasswordEncoder encoder;
	
	@Autowired
	JwtUtil jwtUtils;

	@PostMapping("/login")
	public ApiResponse<String> authenticateUser(@RequestBody Users user) {
		try {
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			String token = jwtUtils.generateToken(userDetails.getUsername());
			return ApiResponse.success("Connexion réussie", token);
		} catch (Exception e) {
			return ApiResponse.error("Échec de l'authentification", "Nom d'utilisateur ou mot de passe incorrect");
		}
	}
	
	//http://localhost:8080/travel/api/auth/signup
	@PostMapping("/signup")
	public ApiResponse<String> registerUser(@RequestBody Users user) {
		if (repos.existsByUserName(user.getUserName())) {
			return ApiResponse.error("Erreur d'inscription", "Le nom d'utilisateur est déjà pris!");
		}
		try {
			Users newUser = new Users(user.getEmail(), user.getUserName(), encoder.encode(user.getPassword()), null, null, null, null, null);
			repos.save(newUser);
			return ApiResponse.success("Utilisateur enregistré avec succès!", "Compte créé");
		} catch (Exception e) {
			return ApiResponse.error("Erreur lors de l'inscription", "Impossible de créer le compte");
		}
	}
}