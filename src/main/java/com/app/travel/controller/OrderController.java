package com.app.travel.controller;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.travel.dto.ApiResponse;
import com.app.travel.model.Order;
import com.app.travel.model.Users;
import com.app.travel.repos.OrderRepository;
import com.app.travel.utils.ContextUtil;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/orders")
public class OrderController {
    
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ContextUtil contextUtil;

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<Order>>> getAllOrders() {
        try {
            if (!contextUtil.isAdmin()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Cette ressource n'est pas accessible"));
            }
            List<Order> orders = orderRepository.findAll();
            return ResponseEntity.ok(ApiResponse.success("Commandes récupérées avec succès", orders));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la récupération des commandes"));
        }
    }

    @GetMapping("/mine")
    public ResponseEntity<ApiResponse<List<Order>>> getMyOrders() {
        try {
            Users currentUser = contextUtil.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Utilisateur non trouvé"));
            }       
            List<Order> orders = orderRepository.findByUserUserId(currentUser.getUserId());
            return ResponseEntity.ok(ApiResponse.success("Vos commandes récupérées avec succès", orders));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la récupération de vos commandes"));
        }
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<Order>> createOrder(@RequestBody Order order) {
        try {
            Users currentUser = contextUtil.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Utilisateur non trouvé"));
            }
            order.setUser(currentUser);
            Order savedOrder = orderRepository.save(order);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Commande créée avec succès", savedOrder));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Erreur lors de la création de la commande"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Order>> updateOrder(@PathVariable int id, @RequestBody Order order) {
        try {
            Optional<Order> existingOrderOpt = orderRepository.findById(id);
            if (!existingOrderOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Commande non trouvée avec l'ID: " + id));
            }
            Order existingOrder = existingOrderOpt.get();
            
            if (!contextUtil.canAccessUser(existingOrder.getUser())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Cette ressource n'est pas accessible"));
            }

            order.setOrderId(id);

            if (!contextUtil.isAdmin()) {
                order.setUser(existingOrder.getUser());
            }
            
            Order updatedOrder = orderRepository.save(order);
            return ResponseEntity.ok(ApiResponse.success("Commande mise à jour avec succès", updatedOrder));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la mise à jour de la commande"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<List<Order>>> getOrderById(@PathVariable int id) {
        try {

            Optional<Order> orderOpt = orderRepository.findById(id);
            if (!orderOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Commande non trouvée avec l'ID: " + id));
            }
            
            Order order = orderOpt.get();
            
            if (!contextUtil.canAccessUser(order.getUser())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Cette ressource n'est pas accessible"));
            }
            
            return ResponseEntity.ok(ApiResponse.success("Commande trouvée", List.of(order)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la recherche de la commande"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable int id) {
        try {
            
            Optional<Order> orderOpt = orderRepository.findById(id);
            if (!orderOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Commande non trouvée avec l'ID: " + id));
            }
            
            Order order = orderOpt.get();
            
            if (!contextUtil.canAccessUser(order.getUser())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Cette ressource n'est pas accessible"));
            }
            
            orderRepository.deleteById(id);
            return ResponseEntity.ok(ApiResponse.success("Commande supprimée avec succès"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la suppression de la commande"));
        }
    }

    @GetMapping("/users/{userid}")
    public ResponseEntity<ApiResponse<List<Order>>> getOrdersByUserId(@PathVariable int userid) {
        try {

            if (!contextUtil.canAccessUser(userid)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Cette ressource n'est pas accessible"));
            }
            
            List<Order> orders = orderRepository.findByUserUserId(userid);
            return ResponseEntity.ok(ApiResponse.success("Commandes trouvées pour l'utilisateur: " + userid, orders));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la recherche par utilisateur"));

        }
    }

    @GetMapping("/creationdateafter/{limit}")
    public ResponseEntity<ApiResponse<List<Order>>> getMyOrdersCreatedAfter(@PathVariable Date limit) {
        try {

            Users currentUser = contextUtil.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Utilisateur non trouvé"));
            }
            
            List<Order> orders;
            if (contextUtil.isAdmin()) {
                orders = orderRepository.findByCreationDateAfter(limit);
            } else {
                orders = orderRepository.findByUserUserIdAndCreationDateAfter(currentUser.getUserId(), limit);
            }
            
            return ResponseEntity.ok(ApiResponse.success("Commandes créées après la date: " + limit, orders));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la recherche des commandes par date de création"));
        }
    }

    @GetMapping("/tripstartdateafter/{limit}")
    public ResponseEntity<ApiResponse<List<Order>>> getMyOrdersWithTripStartAfter(@PathVariable Date limit) {
        try {

            Users currentUser = contextUtil.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Utilisateur non trouvé"));
            }   

            List<Order> orders;
            if (contextUtil.isAdmin()) {
                orders = orderRepository.findByTripStartDateAfter(limit);
            } else {
                orders = orderRepository.findByUserUserIdAndTripStartDateAfter(currentUser.getUserId(), limit);
            }
            
            return ResponseEntity.ok(ApiResponse.success("Commandes de voyages commençant après la date: " + limit, orders));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la recherche des commandes par date de début de voyage"));
        }
    }
}