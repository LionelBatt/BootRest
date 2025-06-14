// package com.app.travel.service;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.cache.annotation.Cacheable;
// import org.springframework.data.redis.core.RedisTemplate;
// import org.springframework.stereotype.Service;
// import com.app.travel.repos.TripRepository;
// import java.util.concurrent.TimeUnit;

// /**
//  * Service de cache pour les statistiques et métriques de l'application
//  * Utilise Redis ElastiCache pour améliorer les performances
//  */
// @Service
// public class CacheService {

//     @Autowired
//     private RedisTemplate<String, Object> redisTemplate;

//     @Autowired
//     private TripRepository tripRepository;

//     /**
//      * Compte total des voyages avec cache Redis
//      */
//     @Cacheable(value = "stats", key = "'total-trips'")
//     public Long getTotalTripsCount() {
//         return tripRepository.count();
//     }

//     /**
//      * Vérifie la santé de la connexion Redis
//      */
//     public boolean isRedisHealthy() {
//         try {
//             redisTemplate.opsForValue().set("health-check", "ok", 10, TimeUnit.SECONDS);
//             String result = (String) redisTemplate.opsForValue().get("health-check");
//             return "ok".equals(result);
//         } catch (Exception e) {
//             return false;
//         }
//     }

//     /**
//      * Nettoie le cache pour une clé spécifique
//      */
//     public void evictCache(String cacheName, String key) {
//         try {
//             String cacheKey = cacheName + "::" + key;
//             redisTemplate.delete(cacheKey);
//         } catch (Exception e) {
//             // Log l'erreur mais ne pas faire échouer l'opération
//         }
//     }

//     /**
//      * Nettoie tout le cache d'un type donné
//      */
//     public void evictAllCache(String cacheName) {
//         try {
//             String pattern = cacheName + "::*";
//             redisTemplate.delete(redisTemplate.keys(pattern));
//         } catch (Exception e) {
//             // Log l'erreur mais ne pas faire échouer l'opération
//         }
//     }

//     /**
//      * Obtient des informations sur le cache Redis
//      */
//     public String getCacheInfo() {
//         try {
//             return redisTemplate.getConnectionFactory().getConnection().info().toString();
//         } catch (Exception e) {
//             return "Cache information unavailable: " + e.getMessage();
//         }
//     }

//     /**
//      * Met en cache une valeur personnalisée avec TTL
//      */
//     public void cacheValue(String key, Object value, long ttlMinutes) {
//         try {
//             redisTemplate.opsForValue().set(key, value, ttlMinutes, TimeUnit.MINUTES);
//         } catch (Exception e) {
//             // Log l'erreur mais ne pas faire échouer l'opération
//         }
//     }

//     /**
//      * Récupère une valeur depuis le cache
//      */
//     public Object getCachedValue(String key) {
//         try {
//             return redisTemplate.opsForValue().get(key);
//         } catch (Exception e) {
//             return null;
//         }
//     }
// }
