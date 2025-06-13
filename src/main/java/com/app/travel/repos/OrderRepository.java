package com.app.travel.repos;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.travel.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer>{

    @Query("SELECT o FROM Order o WHERE o.user.userId = :userId")
    List<Order> findByUserId(@Param("userId") int userId);

    @Query("select a from Order a where a.creationDate > :limit")
    List<Order> findByCreationDateAfter(Date limit);

    @Query("select a from Order a where a.tripStartDate > :limit")
    List<Order> findByTripStartDateAfter(Date limit);

}
