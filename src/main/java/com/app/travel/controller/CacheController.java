// package com.app.travel.controller;

// import java.time.Duration;
// import java.util.HashMap;
// import java.util.Map;
// import java.util.Set;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.redis.core.RedisTemplate;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.CrossOrigin;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;

// import com.app.travel.dto.ApiResponse;

// @RestController
// @RequestMapping("/cache")
// @CrossOrigin
// public class CacheController {

//     @Autowired
//     private RedisTemplate<String, Object> redisTemplate;

//     //Test de connexion ElastiCache
//     @GetMapping("/health")
//     public ResponseEntity<ApiResponse<Map<String, Object>>> checkElastiCacheHealth() {
//         Map<String, Object> healthInfo = new HashMap<>();
        
//         try {
//             // Test simple ping
//             String pong = redisTemplate.getConnectionFactory().getConnection().ping();
//             healthInfo.put("ping", pong);
//             healthInfo.put("connected", true);
            
//             // Test lecture/écriture
//             String testKey = "health-check:" + System.currentTimeMillis();
//             String testValue = "ElastiCache OK";
            
//             redisTemplate.opsForValue().set(testKey, testValue, Duration.ofSeconds(10));
//             String retrievedValue = (String) redisTemplate.opsForValue().get(testKey);
//             redisTemplate.delete(testKey);
            
//             healthInfo.put("readWrite", testValue.equals(retrievedValue));
//             healthInfo.put("timestamp", System.currentTimeMillis());
            
//             return ResponseEntity.ok(ApiResponse.success("ElastiCache opérationnel", healthInfo));
            
//         } catch (Exception e) {
//             healthInfo.put("connected", false);
//             healthInfo.put("error", e.getMessage());
//             healthInfo.put("timestamp", System.currentTimeMillis());
            
//             return ResponseEntity.status(503)
//                     .body(ApiResponse.error("ElastiCache non accessible"));
//         }
//     }

//     // Statistiques détaillées ElastiCache
//     @GetMapping("/stats")
//     public ResponseEntity<ApiResponse<Map<String, Object>>> getElastiCacheStats() {
//         Map<String, Object> stats = new HashMap<>();
        
//         try {
//             // Compter les clés par préfixe (nos différents caches)
//             String[] cachePrefixes = {"trips:", "trip-details:", "trip-search:", "destinations:", "orders:"};
            
//             for (String prefix : cachePrefixes) {
//                 Set<String> keys = redisTemplate.keys("travel-agency:" + prefix + "*");
//                 stats.put(prefix.replace(":", ""), keys != null ? keys.size() : 0);
//             }
            
//             // Statistiques globales
//             Set<String> allKeys = redisTemplate.keys("travel-agency:*");
//             stats.put("totalCachedKeys", allKeys != null ? allKeys.size() : 0);
//             stats.put("elastiCacheHealthy", true);
//             stats.put("timestamp", System.currentTimeMillis());
            
//             return ResponseEntity.ok(ApiResponse.success("Statistiques ElastiCache", stats));
            
//         } catch (Exception e) {
//             stats.put("error", e.getMessage());
//             stats.put("elastiCacheHealthy", false);
            
//             return ResponseEntity.status(500)
//                     .body(ApiResponse.error("Erreur statistiques ElastiCache"));
//         }
//     }

//     //Vider un cache spécifique
//     @DeleteMapping("/clear/{cacheType}")
//     public ResponseEntity<ApiResponse<Map<String, Object>>> clearCacheType(@PathVariable String cacheType) {
//         Map<String, Object> result = new HashMap<>();
        
//         try {
//             String pattern = "travel-agency:" + cacheType + ":*";
//             Set<String> keys = redisTemplate.keys(pattern);
            
//             if (keys != null && !keys.isEmpty()) {
//                 Long deletedCount = redisTemplate.delete(keys);
//                 result.put("pattern", pattern);
//                 result.put("keysDeleted", deletedCount);
//                 result.put("timestamp", System.currentTimeMillis());
                
//                 return ResponseEntity.ok(ApiResponse.success(
//                     "🗑️ Cache '" + cacheType + "' vidé - " + deletedCount + " clés supprimées", result));
//             } else {
//                 result.put("pattern", pattern);
//                 result.put("keysDeleted", 0);
                
//                 return ResponseEntity.ok(ApiResponse.success(
//                     "ℹ️ Aucune clé trouvée pour le cache '" + cacheType + "'", result));
//             }
            
//         } catch (Exception e) {
//             result.put("error", e.getMessage());
//             result.put("cacheType", cacheType);
            
//             return ResponseEntity.status(500)
//                     .body(ApiResponse.error("Erreur lors du vidage du cache"));
//         }
//     }

//     //Vider TOUT le cache de l'application
//     @DeleteMapping("/clear-all")
//     public ResponseEntity<ApiResponse<Map<String, Object>>> clearAllCache() {
//         Map<String, Object> result = new HashMap<>();
        
//         try {
//             String pattern = "travel-agency:*";
//             Set<String> keys = redisTemplate.keys(pattern);
            
//             if (keys != null && !keys.isEmpty()) {
//                 Long deletedCount = redisTemplate.delete(keys);
//                 result.put("pattern", pattern);
//                 result.put("totalKeysDeleted", deletedCount);
//                 result.put("timestamp", System.currentTimeMillis());
                
//                 return ResponseEntity.ok(ApiResponse.success(
//                     "🗑️ Tous les caches vidés - " + deletedCount + " clés supprimées", result));
//             } else {
//                 result.put("totalKeysDeleted", 0);
                
//                 return ResponseEntity.ok(ApiResponse.success("Aucun cache à vider", result));
//             }
            
//         } catch (Exception e) {
//             result.put("error", e.getMessage());
            
//             return ResponseEntity.status(500)
//                     .body(ApiResponse.error("Erreur lors du vidage complet"));
//         }
//     }

//     //Test de performance ElastiCache
//     @PostMapping("/performance-test")
//     public ResponseEntity<ApiResponse<Map<String, Object>>> performanceTest(
//             @RequestParam(defaultValue = "100") int operations) {
        
//         Map<String, Object> perfResults = new HashMap<>();
        
//         try {
//             long startTime = System.currentTimeMillis();
            
//             // Test d'écriture
//             long writeStart = System.currentTimeMillis();
//             for (int i = 0; i < operations; i++) {
//                 redisTemplate.opsForValue().set(
//                     "perf-test:" + i, 
//                     "Test value " + i, 
//                     Duration.ofMinutes(1)
//                 );
//             }
//             long writeTime = System.currentTimeMillis() - writeStart;
            
//             // Test de lecture
//             long readStart = System.currentTimeMillis();
//             for (int i = 0; i < operations; i++) {
//                 redisTemplate.opsForValue().get("perf-test:" + i);
//             }
//             long readTime = System.currentTimeMillis() - readStart;
            
//             // Nettoyage
//             Set<String> testKeys = redisTemplate.keys("perf-test:*");
//             if (testKeys != null && !testKeys.isEmpty()) {
//                 redisTemplate.delete(testKeys);
//             }
            
//             long totalTime = System.currentTimeMillis() - startTime;
            
//             perfResults.put("operations", operations);
//             perfResults.put("writeTimeMs", writeTime);
//             perfResults.put("readTimeMs", readTime);
//             perfResults.put("totalTimeMs", totalTime);
//             perfResults.put("avgWriteMs", (double) writeTime / operations);
//             perfResults.put("avgReadMs", (double) readTime / operations);
//             perfResults.put("operationsPerSecond", (double) (operations * 2) / (totalTime / 1000.0));
            
//             return ResponseEntity.ok(ApiResponse.success(
//                 "⚡ Test de performance ElastiCache terminé", perfResults));
            
//         } catch (Exception e) {
//             perfResults.put("error", e.getMessage());
//             perfResults.put("operations", operations);
            
//             return ResponseEntity.status(500)
//                     .body(ApiResponse.error("Erreur test de performance"));
//         }
//     }

//     //Tester une clé spécifique
//     @GetMapping("/test-key/{key}")
//     public ResponseEntity<ApiResponse<Map<String, Object>>> testSpecificKey(@PathVariable String key) {
//         Map<String, Object> result = new HashMap<>();
        
//         try {
//             String fullKey = "travel-agency:test:" + key;
            
//             // Vérifier si la clé existe
//             Boolean exists = redisTemplate.hasKey(fullKey);
//             result.put("keyExists", exists);
//             result.put("fullKey", fullKey);
            
//             if (Boolean.TRUE.equals(exists)) {
//                 Object value = redisTemplate.opsForValue().get(fullKey);
//                 Long ttl = redisTemplate.getExpire(fullKey);
                
//                 result.put("value", value);
//                 result.put("ttlSeconds", ttl);
//             }
            
//             return ResponseEntity.ok(ApiResponse.success("🔍 Informations sur la clé", result));
            
//         } catch (Exception e) {
//             result.put("error", e.getMessage());
//             result.put("key", key);
            
//             return ResponseEntity.status(500)
//                     .body(ApiResponse.error("Erreur lors de la vérification"));
//         }
//     }

//     //Créer une clé de test
//     @PostMapping("/test-key")
//     public ResponseEntity<ApiResponse<Map<String, Object>>> createTestKey(
//             @RequestParam String key, 
//             @RequestParam String value,
//             @RequestParam(defaultValue = "300") int ttlSeconds) {
        
//         Map<String, Object> result = new HashMap<>();
        
//         try {
//             String fullKey = "travel-agency:test:" + key;
            
//             redisTemplate.opsForValue().set(fullKey, value, Duration.ofSeconds(ttlSeconds));
            
//             result.put("fullKey", fullKey);
//             result.put("value", value);
//             result.put("ttlSeconds", ttlSeconds);
//             result.put("created", true);
            
//             return ResponseEntity.ok(ApiResponse.success("Clé de test créée", result));
            
//         } catch (Exception e) {
//             result.put("error", e.getMessage());
//             result.put("key", key);
//             result.put("created", false);
            
//             return ResponseEntity.status(500)
//                     .body(ApiResponse.error("Erreur création clé test"));
//         }
//     }
// }