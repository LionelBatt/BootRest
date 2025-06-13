package com.app.travel.repos;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.travel.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer>{
    
    List<Order> findByUserId(int userId);

    @Query("select a from Orders a where a.creationDate > :limit")
    List<Order> findByCreationDateAfter(Date limit);

    @Query("select a from Orders a where a.tripStartDate > :limit")
    List<Order> findByTripStartDateAfter(Date limit);

}
