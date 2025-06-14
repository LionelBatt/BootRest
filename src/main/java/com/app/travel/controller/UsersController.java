package com.app.travel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.travel.dto.ApiResponse;
import com.app.travel.model.Users;
import com.app.travel.repos.UserRepository;
import com.app.travel.security.JwtUtil;
import com.app.travel.service.TokenBlacklistService;
import com.app.travel.utils.ContextUtil;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
	UserRepository repos;

    @Autowired
    TokenBlacklistService tokenBlacklistService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    ContextUtil contextUtil;

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<Users>>> getAllUsers(){
        try {
            if (contextUtil.isAdmin()) {
                List<Users> users = repos.findAll();
                if (users.isEmpty()) {
                    return ResponseEntity.ok(ApiResponse.error("Aucun utilisateur trouvé"));
                }
                return ResponseEntity.ok(ApiResponse.success("Liste des utilisateurs", users));
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error("Cette ressource n'est pas accessible"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error("Erreur lors de la récupération des utilisateurs"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Users>> getUserById(@PathVariable int id) {
        try {
            Users user = repos.findById(id).orElse(null);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Aucun utilisateur trouvé avec l'id: " + id));
            }

            if (!contextUtil.canAccessUser(user)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Cette ressource n'est pas accessible"));
            }         
            return ResponseEntity.ok(ApiResponse.success("Utilisateur trouvé", user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la récupération des informations utilisateur"));
        }
    }

    @GetMapping("/profil")
    public ResponseEntity<ApiResponse<Users>> getCurrentUser() {
        try {       
            Users user = contextUtil.getCurrentUser();
            if (user != null) {
                return ResponseEntity.ok(ApiResponse.success("Profil:", user));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Profil non trouvé"));
            }            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la récupération du profil"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Users>> updateUser(@PathVariable int id, @RequestBody Users user) {
        try {
            Users existingUser = repos.findById(id).orElse(null);
            if (existingUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Utilisateur non trouvé avec l'ID: " + id));
            }
            
            if (!contextUtil.canAccessUser(existingUser)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Cette ressource n'est pas accessible"));
            }
            
            user.setUserId(id);
            Users updatedUser = repos.save(user);
            return ResponseEntity.ok(ApiResponse.success("Utilisateur mis à jour avec succès", updatedUser));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la mise à jour des informations"));
        }
    }

@DeleteMapping("/{id}")
public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable int id, HttpServletRequest request) {
    try {
        Users userToDelete = repos.findById(id).orElse(null);
        if (userToDelete == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Utilisateur non trouvé"));
        }
        
        if (!contextUtil.canAccessUser(userToDelete)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("Cette ressource n'est pas accessible"));
        }
        
        Users currentUser = contextUtil.getCurrentUser();
        boolean isSelfDeletion = false;
        
        if (currentUser != null) {
            isSelfDeletion = currentUser.getUserId() == userToDelete.getUserId();
        }
        
        if (isSelfDeletion) {
            String token = jwtUtil.extractTokenFromRequest(request);
            if (token != null) {
                tokenBlacklistService.blacklistToken(token);
            }
        }
        
        repos.deleteById(id);

        String message = isSelfDeletion ? 
            "Votre compte a été supprimé avec succès. Vous êtes maintenant déconnecté." : 
            "Utilisateur supprimé avec succès";
            
        return ResponseEntity.ok(ApiResponse.success(message));
        
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur lors de la suppression de l'utilisateur", e.getMessage()));
    }
}
    
}
