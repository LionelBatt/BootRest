package com.app.travel.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.app.travel.model.Trip;
import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Integer> {

    
    List<Trip> findByDestination_Country(String country);
    List<Trip> findByDestination_Continent(String continent);
    Trip findFirstByDestination_City(String city);

}