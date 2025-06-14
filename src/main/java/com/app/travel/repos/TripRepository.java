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
    List<Trip> findByDestinationCity(@Param("continent") City city);
    
    @Query("SELECT t FROM Trip t WHERE t.DestinationCity = :city")
    Trip findFirstByDestinationCity(@Param("city") City city);

    @Query("SELECT DISTINCT t FROM Trip t JOIN t.orders o WHERE o.user.userId = :userId")
    List<Trip> findByUser_UserId(@Param("userId") Integer userId);

    @Query("SELECT DISTINCT t FROM Trip t JOIN t.packageOptions o WHERE " +
           "(:continent IS NULL OR t.DestinationContinent = :continent) AND " +
           "(:country IS NULL OR t.DestinationCountry = :country) AND " +
           "(:city IS NULL OR t.DestinationCity = :city) AND " +
           "t.minimumDuration >= :minimumDuration AND t.minimumDuration <= :maximumDuration AND " +
           "(:option1 = 0 OR o.optionId = :option1 OR o.optionId = :option2 OR o.optionId = :option3) AND " +
           "t.unitPrice >= :prixmin AND t.unitPrice <= :prixmax")
    List<Trip> findByDestinationCity(@Param("continent") Continent continent, @Param("country") Country country, @Param("city") City city, 
    @Param("minimumDuration") int minimumDuration, @Param("maximumDuration") int maximumDuration, @Param("option1") int option1, 
    @Param("option2") int option2, @Param("option3") int option3, @Param("prixmin") int prixmin, @Param("prixmax") int prixmax);

}