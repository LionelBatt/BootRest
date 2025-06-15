package com.app.travel.service;

import java.util.concurrent.TimeUnit;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.app.travel.repos.TripRepository;

/**
 * Service de cache pour les statistiques et métriques de l'application
 * Utilise Redis conteneur local pour améliorer les performances
 * Migration d'AWS ElastiCache vers conteneur local pour optimisation des coûts
 */
@Service
public class CacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final TripRepository tripRepository;

    public CacheService(RedisTemplate<String, Object> redisTemplate, TripRepository tripRepository) {
        this.redisTemplate = redisTemplate;
        this.tripRepository = tripRepository;
    }

    /**
     * Compte total des voyages avec cache Redis
     */
    @Cacheable(value = "stats", key = "'total-trips'")
    public Long getTotalTripsCount() {
        return tripRepository.count();
    }

    /**
     * Vérifie la santé de la connexion Redis
     */
    public boolean isRedisHealthy() {
        try {
            redisTemplate.opsForValue().set("health-check", "ok", 10, TimeUnit.SECONDS);
            String result = (String) redisTemplate.opsForValue().get("health-check");
            return "ok".equals(result);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Nettoie le cache pour une clé spécifique
     */
    public void evictCache(String cacheName, String key) {
        try {
            String cacheKey = cacheName + "::" + key;
            redisTemplate.delete(cacheKey);
        } catch (Exception e) {
            // Log l'erreur mais ne pas faire échouer l'opération
        }
    }

    /**
     * Nettoie tout le cache d'un type donné
     */
    public void evictAllCache(String cacheName) {
        try {
            String pattern = cacheName + "::*";
            redisTemplate.delete(redisTemplate.keys(pattern));
        } catch (Exception e) {
            // Log l'erreur mais ne pas faire échouer l'opération
        }
    }

    /**
     * Obtient des informations sur le cache Redis conteneur
     */
    public String getCacheInfo() {
        try {
            // Test simple de ping pour vérifier la connexion
            String ping = redisTemplate.getConnectionFactory().getConnection().ping();
            return "Redis Container Status: " + (ping != null ? "Connected" : "Disconnected");
        } catch (Exception e) {
            return "Cache information unavailable: " + e.getMessage();
        }
    }

    /**
     * Met en cache une valeur personnalisée avec TTL
     */
    public void cacheValue(String key, Object value, long ttlMinutes) {
        try {
            redisTemplate.opsForValue().set(key, value, ttlMinutes, TimeUnit.MINUTES);
        } catch (Exception e) {
            // Log l'erreur mais ne pas faire échouer l'opération
        }
    }

    /**
     * Récupère une valeur depuis le cache
     */
    public Object getCachedValue(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            return null;
        }
    }
}
