package com.app.travel.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig {

    @Value("${spring.cache.redis.time-to-live:300000}")
    private long defaultTtl;

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        
        // Configuration des sérialiseurs
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer();
        
        // Clés et Hash keys en String
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);
        
        // Valeurs en JSON
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);
        
        template.setDefaultSerializer(jsonSerializer);
        template.afterPropertiesSet();
        
        return template;
    }

    /**
     * Cache Manager avec TTL personnalisés par type de cache
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMillis(defaultTtl))
                .serializeKeysWith(org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .disableCachingNullValues();

        // Configuration spécifique par cache
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                // Cache pour les voyages - TTL 5 minutes
                .withCacheConfiguration("trips", config.entryTtl(Duration.ofMinutes(5)))
                .withCacheConfiguration("trip-details", config.entryTtl(Duration.ofMinutes(5)))
                // Cache pour les recherches - TTL 2 minutes
                .withCacheConfiguration("trip-search", config.entryTtl(Duration.ofMinutes(2)))
                // Cache pour les destinations - TTL 10 minutes (plus stable)
                .withCacheConfiguration("destinations", config.entryTtl(Duration.ofMinutes(10)))
                // Cache pour les statistiques - TTL 1 minute
                .withCacheConfiguration("stats", config.entryTtl(Duration.ofMinutes(1)))
                .build();
    }
}
