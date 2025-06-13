package com.app.travel.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.travel.model.City;
import com.app.travel.model.Trip;

public interface TripRepository extends JpaRepository<Trip, Integer> {

    List<Trip> findByDestination(City destination);

    //Bug corrigé?

    List<Trip> findByDestination_Country(String country);
    List<Trip> findByDestination_Continent(String continent);
    Trip findFirstByDestination_City(String city);

    
    List<Trip> findByUser_UserId(Integer userId);

}