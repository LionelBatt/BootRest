/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.app.travel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.travel.dto.ApiResponse;
import com.app.travel.model.Users;
import com.app.travel.repos.UserRepository;
import com.app.travel.security.JwtUtil;
import com.app.travel.service.TokenBlacklistService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
	UserRepository repos;

    @Autowired
    TokenBlacklistService tokenBlacklistService;

    @Autowired
    JwtUtil jwtUtil;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Users>> findUserInformation(@PathVariable int id){
        try {
            Users user = repos.findById(id).orElse(null);
            if(user != null){
                return ResponseEntity.ok(ApiResponse.success(true, user));
            }
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error("Erreur lors de la récupération des informations utilisateur", e.getMessage()));
        }
        return ResponseEntity.ok(ApiResponse.error("Aucun utilisateur trouvé avec l'id: " + id));
    }

    @PutMapping("")
    @PreAuthorize("hasRole('ADMIN') or #user.userId == authentication.principal.userId")
    public ResponseEntity<ApiResponse<Users>> updateUser(@RequestBody Users user) {
        try {
            Users updatedUser = repos.save(user);
            return ResponseEntity.ok(ApiResponse.success("Utilisateur mis à jour avec succès", updatedUser));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error("Erreur lors de la mise à jour des informations", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.userId")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable int id, HttpServletRequest request) {
        try {
            // Récupérer l'utilisateur connecté
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserDetails currentUser = (UserDetails) auth.getPrincipal();
            
            // Récupérer l'utilisateur à supprimer
            Users userToDelete = repos.findById(id).orElse(null);
            if (userToDelete == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Utilisateur non trouvé"));
            }
            
            // Vérifier si l'utilisateur supprime son propre compte
            boolean isSelfDeletion = userToDelete.getUsername().equals(currentUser.getUsername());
            String token = jwtUtil.extractTokenFromRequest(request);
            if (token != null) {
                // Si c'est une suppression de compte par l'utilisateur lui-même, ajouter le token à la liste noire
                if (isSelfDeletion) {
                    tokenBlacklistService.blacklistToken(token);
                }
            }
            repos.deleteById(id);

            String message = isSelfDeletion ? "Votre compte a été supprimé avec succès. Vous êtes maintenant déconnecté." : "Utilisateur supprimé avec succès";
                
            return ResponseEntity.ok(ApiResponse.success(message));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error("Erreur lors de la suppression de l'utilisateur", e.getMessage()));
        }
    }
    
}
