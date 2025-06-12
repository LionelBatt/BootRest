package com.app.travel.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "login_attempts")
public class LoginAttempt {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(nullable = false)
    private Integer attempts = 0;
    
    @Column(name = "locked_until")
    private LocalDateTime lockedUntil;
    
    public LoginAttempt() {}
    
    public LoginAttempt(String username) {
        this.username = username;
    }
    
    // Getters et Setters
    public Long getId() { 
        return id; 
    }
    public void setId(Long id) { 
        this.id = id; 
    }
    
    public String getUsername() { 
        return username; 
    }
    public void setUsername(String username) { 
        this.username = username; 
    }
    
    public Integer getAttempts() { 
        return attempts; 
    }
    public void setAttempts(Integer attempts) { 
        this.attempts = attempts; 
    }
    
    public LocalDateTime getLockedUntil() { 
        return lockedUntil; 
    }
    public void setLockedUntil(LocalDateTime lockedUntil) { 
        this.lockedUntil = lockedUntil; 
    }
    
    // incrementer le nombre de tentatives
    public void incrementAttempts() {
        this.attempts++;
    }

    //vérifier si le compte est verrouillé
    public boolean isLocked() {
        return lockedUntil != null && LocalDateTime.now().isBefore(lockedUntil);
    }
}
