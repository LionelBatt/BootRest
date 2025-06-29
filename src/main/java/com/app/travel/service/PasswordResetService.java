package com.app.travel.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.travel.model.ResetToken;
import com.app.travel.model.Users;
import com.app.travel.repos.ResetTokenRepository;
import com.app.travel.repos.UserRepository;
import com.app.travel.utils.Util;

@Service
public class PasswordResetService {

    @Autowired
    UserRepository userRepos;

    @Autowired
    ResetTokenRepository resetTokenRepos;
    

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void forgotPassword(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        Optional<ResetToken> existingToken = resetTokenRepos.findByEmail(email);

        existingToken.ifPresent(t -> {
            t.setTokenUsed(true);
            resetTokenRepos.save(t);
        });

        String token = Util.generateToken();
        Date expireDate = new Date(System.currentTimeMillis() + 3600000);

        ResetToken resetToken = new ResetToken();
        resetToken.setEmail(email);
        resetToken.setToken(token);
        resetToken.setExpiration(expireDate);
        resetToken.setTokenUsed(false);

        resetTokenRepos.save(resetToken);

    }

    public void changePassword(String email, String token, String password) {
        Optional<ResetToken> resetToken = resetTokenRepos.findByEmailAndTokenAndTokenUsed(email, token, false);
        if (resetToken.isPresent()) {
            ResetToken entityToken = resetToken.get();
            entityToken.setTokenUsed(true);
            resetTokenRepos.save(entityToken);

            Users user = userRepos.findByEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            userRepos.save(user);
        } else {
            throw new IllegalArgumentException("Invalid or expired token");
        }
    }

    public Optional<ResetToken> validateToken(String email, String token) {
        Optional<ResetToken> resetToken = resetTokenRepos.findByEmailAndTokenAndTokenUsed(email, token, false);
        if (resetToken.isPresent()) {
            return resetToken;
        } else {
            throw new IllegalArgumentException("Invalid or expired token");
        }
    }
}
