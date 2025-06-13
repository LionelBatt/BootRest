package com.app.travel.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.travel.model.City;
import com.app.travel.model.Continent;
import com.app.travel.model.Country;
import com.app.travel.model.Trip;

public interface TripRepository extends JpaRepository<Trip, Integer> {

    @Query("SELECT t FROM Trip t WHERE t.DestinationCountry = :country")
    List<Trip> findByDestinationCountry(@Param("country") Country country);
    
    @Query("SELECT t FROM Trip t WHERE t.DestinationContinent = :continent")
    List<Trip> findByDestinationContinent(@Param("continent") Continent continent);
    
    @Query("SELECT t FROM Trip t WHERE t.DestinationCity = :city")
    Trip findFirstByDestinationCity(@Param("city") City city);

    @Query("SELECT DISTINCT t FROM Trip t JOIN t.orders o WHERE o.user.userId = :userId")
    List<Trip> findByUser_UserId(@Param("userId") Integer userId);

}