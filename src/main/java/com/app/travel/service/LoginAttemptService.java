package com.app.travel.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.travel.model.LoginAttempt;
import com.app.travel.repos.LoginAttemptRepository;

@Service
@Transactional
public class LoginAttemptService {
    
    private static final int MAX_ATTEMPTS = 5;
    private static final int LOCK_TIME_MINUTES = 15;
    
    @Autowired
    private LoginAttemptRepository repository;
    
    public void loginSucceeded(String username) {
        repository.deleteByUsername(username);
    }
    
    public void loginFailed(String username) {
        LoginAttempt attempt = repository.findByUsername(username)
            .orElse(new LoginAttempt(username));
        
        attempt.incrementAttempts();
        
        if (attempt.getAttempts() >= MAX_ATTEMPTS) {
            attempt.setLockedUntil(LocalDateTime.now().plusMinutes(LOCK_TIME_MINUTES));
        }
        
        repository.save(attempt);
    }
    
    public boolean isBlocked(String username) {
        return repository.findByUsername(username)
            .map(attempt -> {
                if (attempt.isLocked()) {
                    return true;
                } else if (attempt.getLockedUntil() != null) {
                    // Verrouillage expiré, nettoyer
                    repository.deleteByUsername(username);
                    return false;
                }
                return attempt.getAttempts() >= MAX_ATTEMPTS;
            })
            .orElse(false);
    }
    
    public int getRemainingAttempts(String username) {
        return repository.findByUsername(username)
            .map(attempt -> Math.max(0, MAX_ATTEMPTS - attempt.getAttempts()))
            .orElse(MAX_ATTEMPTS);
    }
    
    public LocalDateTime getUnlockTime(String username) {
        return repository.findByUsername(username)
            .map(LoginAttempt::getLockedUntil)
            .orElse(null);
    }
}
