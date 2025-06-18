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
import com.app.travel.repos.OrderRepository;
import com.app.travel.repos.UserRepository;
import com.app.travel.security.JwtUtil;
import com.app.travel.service.TokenBlacklistService;
import com.app.travel.utils.ContextUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    UserRepository repos;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ContextUtil contextUtil;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    /**
     * Endpoint pour récupérer tous les utilisateurs.
     * Accessible uniquement par les administrateurs.
     *
     * @return Liste des utilisateurs ou un message d'erreur.
     */
    @GetMapping("")
    public ResponseEntity<ApiResponse<List<Users>>> getAllUsers() {
        try {
            if (contextUtil.isAdmin()) {
                List<Users> users = repos.findAll();
                if (users.isEmpty()) {
                    return ResponseEntity.ok(ApiResponse.error("Aucun utilisateur trouvé"));
                }
                return ResponseEntity.ok(ApiResponse.success("Liste des utilisateurs", users));
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Cette ressource n'est pas accessible"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la récupération des utilisateurs"));
        }
    }

    /**
     * Endpoint pour récupérer un utilisateur par son ID.
     * Vérifie les permissions d'accès.
     *
     * @param id L'ID de l'utilisateur à récupérer.
     * @return L'utilisateur ou un message d'erreur.
     */
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
    /**
     * Endpoint pour récupérer le profil de l'utilisateur actuellement connecté.
     * Vérifie si l'utilisateur est authentifié.
     *
     * @return Le profil de l'utilisateur ou un message d'erreur.
     */
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
    /**
     * Endpoint pour mettre à jour le profil de l'utilisateur actuellement connecté.
     * Vérifie si l'utilisateur est authentifié.
     *
     * @param user Les nouvelles informations de l'utilisateur.
     * @return L'utilisateur mis à jour ou un message d'erreur.
     */
    @PutMapping("/profil")
    public ResponseEntity<ApiResponse<Users>> updateCurrentUserProfile(@RequestBody Users user) {
        try {
            Users currentUser = contextUtil.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("Utilisateur non authentifié"));
            }     
            user.setUserId(currentUser.getUserId());
            user.setRole(currentUser.getRole());
            user.setVersion(currentUser.getVersion());
            if (user.getCardInfo() != null && currentUser.getCardInfo() != null) {
                user.getCardInfo().setId(currentUser.getCardInfo().getId());
            }
            Users updatedUser = repos.save(user);
            return ResponseEntity.ok(ApiResponse.success("Profil mis à jour avec succès", updatedUser));
            
        } catch (Exception e) {
            // Log l'erreur pour le débogage
            System.err.println("Erreur lors de la mise à jour du profil: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la mise à jour du profil: " + e.getMessage()));
        }
    }

    /**
     * Endpoint pour mettre à jour un utilisateur par son ID.
     * Accessible uniquement par les administrateurs.
     *
     * @param id L'ID de l'utilisateur à mettre à jour.
     * @param user Les nouvelles informations de l'utilisateur.
     * @return L'utilisateur mis à jour ou un message d'erreur.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Users>> updateUser(@PathVariable int id, @RequestBody Users user) {
        try {
            // Récupérer l'utilisateur courant      
            if (!contextUtil.isAdmin()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Accès refusé : privilèges administrateur requis"));
            }  
            Users existingUser = repos.findById(id).orElse(null);
            if (existingUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Utilisateur non trouvé avec l'ID: " + id));
            }
            user.setUserId(id);
            Users updatedUser = repos.save(user);
            return ResponseEntity.ok(ApiResponse.success("Utilisateur mis à jour avec succès", updatedUser));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la mise à jour des informations"));
        }
    }

        /**
         * Endpoint pour supprimer un utilisateur.
         * Vérifie les permissions d'accès et gère la suppression de l'utilisateur actuel.
         *
         * @param id L'ID de l'utilisateur à supprimer.
         * @return Un message de succès ou d'erreur.
         */
    @DeleteMapping("/{id}")
    @Transactional
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

            orderRepository.deleteByUserUserId(id);

            // Gérer la blacklist du token pour l'auto-suppression
            if (isSelfDeletion) {
                String token = jwtUtil.extractTokenFromRequest(request);
                if (token != null) {
                    tokenBlacklistService.blacklistToken(token);
                }
            }

            // Supprimer l'utilisateur
            repos.deleteById(id);

            String message = isSelfDeletion
                    ? "Votre compte et toutes vos commandes ont été supprimés avec succès. Vous êtes maintenant déconnecté."
                    : "Utilisateur et ses commandes supprimés avec succès";

            return ResponseEntity.ok(ApiResponse.success(message));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la suppression de l'utilisateur: " + e.getMessage()));
        }
    }

}
