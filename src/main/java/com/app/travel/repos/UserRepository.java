package com.app.travel.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.travel.model.Users;

public interface UserRepository extends JpaRepository<Users, Integer> {

	Optional<Users> findByUsername(String username);

	Users findByEmail(String email);

	Users findByPhoneNumber(String phoneNumber);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	boolean existsByPhoneNumber(String phoneNumber);

}
