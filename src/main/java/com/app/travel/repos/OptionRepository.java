package com.app.travel.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.travel.model.Category;
import com.app.travel.model.Option;

@Repository
public interface OptionRepository extends JpaRepository<Option, Integer> {
    
    List<Option> findByCategory(Category category);
    
    List<Option> findByPrixBetween(double minPrice, double maxPrice);
    
    List<Option> findByPrixLessThanEqual(double maxPrice);
}