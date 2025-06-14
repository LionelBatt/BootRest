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
    List<Trip> findByDestinationCity(@Param("city") City city);
    
    @Query("SELECT t FROM Trip t WHERE t.DestinationCity = :city")
    Trip findFirstByDestinationCity(@Param("city") City city);

    @Query("SELECT DISTINCT t FROM Trip t JOIN t.orders o WHERE o.user.userId = :userId")
    List<Trip> findByUser_UserId(@Param("userId") Integer userId);

    @Query("SELECT t FROM Trip t JOIN t.packageOptions o WHERE t.DestinationContinent = :continent AND t.DestinationCountry = :country "
    +"AND t.DestinationCity = :city AND t.minimumDuration > :minimumDuration AND t.minimumDuration < :maximumDuration AND "
    +"o.optionId IN (:option1, :option2, :option3) AND t.unitPrice >= :prixmin AND t.unitPrice <= :prixmax" )
    List<Trip> findByDestinationCityWithOptions(@Param("continent") Continent continent, @Param("country") Country country, @Param("city") City city, 
    @Param("minimumDuration") int minimumDuration, @Param("maximumDuration") int maximumDuration, @Param("option1") int option1, 
    @Param("option2") int option2, @Param("option3") int option3, @Param("prixmin") int prixmin, @Param("prixmax") int prixmax);

    @Query("SELECT t FROM Trip t WHERE LOWER(t.DestinationCity) LIKE LOWER(CONCAT('%', :character, '%')) " +
           "OR LOWER(t.DestinationCountry) LIKE LOWER(CONCAT('%', :character, '%')) " +
           "OR LOWER(t.DestinationContinent) LIKE LOWER(CONCAT('%', :character, '%'))")
    List<Trip> findByCharacter(@Param("character") String character);
}