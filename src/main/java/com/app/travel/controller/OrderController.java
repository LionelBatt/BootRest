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
import com.app.travel.repos.OrderRepository;

@CrossOrigin
@RestController
@RequestMapping("/orders")
public class OrderController {
    
    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<Order>>> findAll() {
        try {
            List<Order> orders = orderRepository.findAll();
            return ResponseEntity.ok(ApiResponse.success("Commandes récupérés avec succès", orders));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la récupération des commandes", e.getMessage()));
        }
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<Order>> create(@RequestBody Order order) {
        try {
            Order savedOrder = orderRepository.save(order);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Commande créé avec succès", savedOrder));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Erreur lors de la création du Commande", e.getMessage()));
        }
    }

    @PutMapping("")
    public ResponseEntity<ApiResponse<Order>> update(@RequestBody Order order) {
        try {
            if (order.getCommandId() == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("ID de la commande requis pour la mise à jour"));
            }
            
            Optional<Order> existingorder = orderRepository.findById(order.getCommandId());
            if (existingorder.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Commande non trouvé avec l'ID: " + order.getCommandId()));
            }

            Order updatedOrder = orderRepository.save(order);
            return ResponseEntity.ok(ApiResponse.success("Commande mis à jour avec succès", updatedOrder));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la mise à jour de la commande", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Order>> findById(@PathVariable int id) {
        try {
            Optional<Order> order = orderRepository.findById(id);
            if (order.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success("Commande trouvée", order.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Commande non trouvée avec l'ID: " + id));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la recherche de la commande", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable int id) {
        try {
            Optional<Order> existingorder = orderRepository.findById(id);
            if (existingorder.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Commande non trouvée avec l'ID: " + id));
            }
            
            orderRepository.deleteById(id);
            return ResponseEntity.ok(ApiResponse.success("Commande supprimée avec succès"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la suppression de la commande", e.getMessage()));
        }
    }

    @GetMapping("/userid/{userid}")
    public ResponseEntity<ApiResponse<List<Order>>> findByUserId(@PathVariable int userid) {
        try {
            List<Order> orders = orderRepository.findByUserId(userid);
            return ResponseEntity.ok(ApiResponse.success("Commande trouvées pour l'utilisateur: " + userid, orders));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Utilisateur invalide: " + userid + "."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la recherche par Utilisateur", e.getMessage()));
        }
    }

    @GetMapping("/creationdateafter/{limit}")
    public ResponseEntity<ApiResponse<List<Order>>> findByCreationDateAfter(@PathVariable Date limit) {
        try {
            List<Order> orders = orderRepository.findByCreationDateAfter(limit);
            return ResponseEntity.ok(ApiResponse.success("Commandes créé après la date: " + limit, orders));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la recherche des commandes par date de creation", e.getMessage()));
        }
    }

    @GetMapping("/tripstartdateafter/{limit}")
    public ResponseEntity<ApiResponse<List<Order>>> findByTripStartDateAfter(@PathVariable Date limit) {
        try {
            List<Order> orders = orderRepository.findByTripStartDateAfter(limit);
            return ResponseEntity.ok(ApiResponse.success("Commandes de voyages commencant après la date: " + limit, orders));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la recherche des commandes par date de début de voyage", e.getMessage()));
        }
    }
}
