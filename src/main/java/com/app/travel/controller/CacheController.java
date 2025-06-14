package com.app.travel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.travel.dto.ApiResponse;
import com.app.travel.service.CacheService;

@RestController
@RequestMapping("/cache")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CacheController {

    @Autowired
    private CacheService cacheService;

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Boolean>> checkRedisHealth() {
        try {
            boolean isHealthy = cacheService.isRedisHealthy();
            if (isHealthy) {
                return ResponseEntity.ok(ApiResponse.success("Redis ElastiCache est opérationnel", true));
            } else {
                return ResponseEntity.status(503)
                        .body(ApiResponse.<Boolean>error("Redis ElastiCache n'est pas accessible"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.<Boolean>error("Erreur lors de la vérification Redis"));
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Object>> getCacheStats() {
        try {
            Long totalTrips = cacheService.getTotalTripsCount();
            String cacheInfo = cacheService.getCacheInfo();
            
            return ResponseEntity.ok(ApiResponse.success("Statistiques du cache", 
                java.util.Map.of(
                    "totalTrips", totalTrips,
                    "redisHealthy", cacheService.isRedisHealthy(),
                    "cacheInfo", cacheInfo
                )));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Erreur lors de la récupération des statistiques", null));
        }
    }

    /**
     * Vide le cache pour un type donné
     */
    @DeleteMapping("/evict/{cacheName}")
    public ResponseEntity<ApiResponse<String>> evictCache(@PathVariable String cacheName) {
        try {
            cacheService.evictAllCache(cacheName);
            return ResponseEntity.ok(ApiResponse.success("Cache '" + cacheName + "' vidé avec succès", cacheName));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Erreur lors de la suppression du cache", e.getMessage()));
        }
    }

    /**
     * Vide une clé spécifique du cache
     */
    @DeleteMapping("/evict/{cacheName}/{key}")
    public ResponseEntity<ApiResponse<String>> evictCacheKey(@PathVariable String cacheName, @PathVariable String key) {
        try {
            cacheService.evictCache(cacheName, key);
            return ResponseEntity.ok(ApiResponse.success("Clé '" + key + "' supprimée du cache '" + cacheName + "'", key));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Erreur lors de la suppression de la clé", e.getMessage()));
        }
    }

    /**
     * Met en cache une valeur de test
     */
    @PostMapping("/test")
    public ResponseEntity<ApiResponse<String>> testCache(@RequestParam String key, @RequestParam String value) {
        try {
            cacheService.cacheValue("test:" + key, value, 5); // 5 minutes TTL
            return ResponseEntity.ok(ApiResponse.success("Valeur mise en cache avec succès", "test:" + key));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Erreur lors de la mise en cache", e.getMessage()));
        }
    }

    /**
     * Récupère une valeur de test du cache
     */
    @GetMapping("/test/{key}")
    public ResponseEntity<ApiResponse<Object>> getTestValue(@PathVariable String key) {
        try {
            Object value = cacheService.getCachedValue("test:" + key);
            if (value != null) {
                return ResponseEntity.ok(ApiResponse.success("Valeur trouvée dans le cache", value));
            } else {
                return ResponseEntity.status(404)
                        .body(ApiResponse.error("Valeur non trouvée dans le cache", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.error("Erreur lors de la récupération", e.getMessage()));
        }
    }
}
