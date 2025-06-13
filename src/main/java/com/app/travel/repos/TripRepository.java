package com.app.travel.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.travel.model.City;
import com.app.travel.model.Continent;
import com.app.travel.model.Country;
import com.app.travel.model.Trip;

public interface TripRepository extends JpaRepository<Trip, Integer> {

    List<Trip> findByDestination(City destination);

    //Bug corrigé?

    List<Trip> findByDestination_Country(Country country);
    List<Trip> findByDestination_Continent(Continent continent);
    List<Trip> findByDestination_City(City city);
    //Trip findFirstByDestination_City(City city);

    
    List<Trip> findByUser_UserId(Integer userId);

}