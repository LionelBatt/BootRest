package com.app.resto.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.resto.model.Users;

public interface UserRepository extends JpaRepository <Users,  Integer> {

	 Users findByUserName(String userName);

	 Users findByEmail(String email);

	 Users findByPhoneNumber(String phoneNumber);

	boolean existsByUserName(String userName);

	boolean existsByEmail(String email);

	boolean existsByPhoneNumber(String phoneNumber);

}
