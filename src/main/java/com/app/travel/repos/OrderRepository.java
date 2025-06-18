package com.app.travel.repos;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.travel.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer>{
    
    @Query("SELECT o FROM Order o JOIN FETCH o.user JOIN FETCH o.trip LEFT JOIN FETCH o.options WHERE o.user.userId = :userId")
    List<Order> findByUserUserIdWithDetails(@Param("userId") Integer userId);
    
    @Query("SELECT o FROM Order o JOIN FETCH o.user JOIN FETCH o.trip LEFT JOIN FETCH o.options")
    List<Order> findAllWithDetails();
    
    @Query("SELECT o FROM Order o JOIN FETCH o.user JOIN FETCH o.trip LEFT JOIN FETCH o.options WHERE o.orderId = :orderId")
    List<Order> findByIdWithDetails(@Param("orderId") Integer orderId);
    
    // Méthodes originales conservées pour compatibilité
    List<Order> findByUserUserId(Integer userId);
    List<Order> findByCreationDateAfter(Date date);
    List<Order> findByTripStartDateAfter(Date date);
    
    List<Order> findByUserUserIdAndCreationDateAfter(Integer userId, Date date);
    List<Order> findByUserUserIdAndTripStartDateAfter(Integer userId, Date date);

}
