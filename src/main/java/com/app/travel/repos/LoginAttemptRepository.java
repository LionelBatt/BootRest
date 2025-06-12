package com.app.travel.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.travel.model.LoginAttempt;

@Repository
public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Integer> {

    Optional<LoginAttempt> findByUsername(String username);
    void deleteByUsername(String username);
}
