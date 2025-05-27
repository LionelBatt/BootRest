package com.app.resto.controller;

import com.app.resto.model.Users;
import com.app.resto.repos.UserRepository;
import com.app.resto.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UserRepository repos;
	
	@Autowired
	PasswordEncoder encoder;
	
	@Autowired
	JwtUtil jwtUtils;

	@PostMapping("/signin")
	public String authenticateUser(@RequestBody Users user) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		return jwtUtils.generateToken(userDetails.getUsername());
	}
	
	//http://localhost:8080/resto/api/auth/signup
	@PostMapping("/signup")
	public String registerUser(@RequestBody Users user) {
		if (repos.existsByUserName(user.getUserName())) {
			return "Error: Username is already taken!";
		}
		// Create new user's account
		Users newUser = new Users(user.getEmail(), user.getUserName(), encoder.encode(user.getPassword()));
		repos.save(newUser);
		return "User registered successfully!";
	}
}