package com.app.travel.service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

@Service
public class TokenBlacklistService {
    
    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    public TokenBlacklistService() {
        executorService.scheduleAtFixedRate(this::cleanupExpiredTokens, 1, 1, TimeUnit.HOURS);
    }
    
    public void blacklistToken(String token){
        blacklistedTokens.add(token);
    }

    public boolean isBlacklisted(String token){
        return blacklistedTokens.contains(token);
    }

    public void cleanupExpiredTokens(){
        blacklistedTokens.clear();
    }
}