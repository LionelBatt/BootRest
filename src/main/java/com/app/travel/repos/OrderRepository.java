package com.app.travel.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.travel.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer>{
    
    List<Order> findByUserId(int userId);

}
