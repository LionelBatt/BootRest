package com.app.travel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.travel.dto.ApiResponse;
import com.app.travel.repos.TripRepository;

@CrossOrigin
@RestController
@RequestMapping("/stats")
public class StatsController {

    @Autowired
    private TripRepository tripRepository;

    // @Autowired
    // private OrderRepository orderRepository;


    @GetMapping("/continents")
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<Object>> getContinentStats() {
        try {
            List<Object[]> stats = tripRepository.countTripsByContinent();
            return ResponseEntity.ok(ApiResponse.success("Statistiques par continent récupérées", stats));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la récupération des statistiques"));
        }
    }

    
}
