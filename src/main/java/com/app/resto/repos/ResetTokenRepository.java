package com.app.resto.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.resto.model.ResetToken;

public interface ResetTokenRepository extends JpaRepository<ResetToken, Integer> {
    Optional<ResetToken> findByEmail(String email);
    Optional<ResetToken> findByEmailAndTokenAndTokenUsed (String email, String token, boolean tokenUsed); 
}