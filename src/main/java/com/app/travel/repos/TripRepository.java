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


    List<Trip> findByDestinationContinent(Continent continent);
    List<Trip> findByDestinationCountry(Country country); 
    List<Trip> findByDestinationCity(City city);

   @Query("SELECT DISTINCT t FROM Trip t JOIN t.orders o WHERE o.user.userId = :userId")
    List<Trip> findByUser_UserId(@Param("userId") Integer userId);

    @Query("SELECT DISTINCT t FROM Trip t LEFT JOIN t.packageOptions o WHERE " +
           "(:continent IS NULL OR t.destinationContinent = :continent) AND " +
           "(:country IS NULL OR t.destinationCountry = :country) AND " +
           "(:city IS NULL OR t.destinationCity = :city) AND " +
           "t.minimumDuration >= :minimumDuration AND t.minimumDuration <= :maximumDuration AND " +
           "t.unitPrice >= :prixmin AND t.unitPrice <= :prixmax AND " +
           "(:options IS NULL OR :optionsEmpty = true OR o.optionId IN (:options))")
    List<Trip> findByDestinationCityWithOptions(@Param("continent") Continent continent, @Param("country") Country country, @Param("city") City city, 
    @Param("minimumDuration") int minimumDuration, @Param("maximumDuration") int maximumDuration, @Param("options") List<Integer> options, 
    @Param("optionsEmpty") boolean optionsEmpty, @Param("prixmin") int prixmin, @Param("prixmax") int prixmax);

    @Query("SELECT t FROM Trip t WHERE " +
           "LOWER(CAST(t.destinationCity AS string)) LIKE LOWER(CONCAT('%', :character, '%')) OR " +
           "LOWER(CAST(t.destinationCountry AS string)) LIKE LOWER(CONCAT('%', :character, '%')) OR " +
           "LOWER(CAST(t.destinationContinent AS string)) LIKE LOWER(CONCAT('%', :character, '%')) OR " +
           "LOWER(t.description) LIKE LOWER(CONCAT('%', :character, '%'))")
    List<Trip> findByCharacter(@Param("character") String character);
    
        /* Trouve les voyages par continent et pays */
    List<Trip> findByDestinationContinentAndDestinationCountry(
        Continent continent, Country country);
    
    /* Trouve les voyages par continent, pays et ville*/
    List<Trip> findByDestinationContinentAndDestinationCountryAndDestinationCity(
        Continent continent, Country country, City city);
    
    /*Trouve les voyages par pays et ville*/
    List<Trip> findByDestinationCountryAndDestinationCity(Country country, City city);
    
    /* Compte le nombre de voyages par continent*/
    @Query("SELECT t.destinationContinent, COUNT(t) FROM Trip t GROUP BY t.destinationContinent")
    List<Object[]> countTripsByContinent();
    
    /*Compte le nombre de voyages par pays*/
    @Query("SELECT t.destinationCountry, COUNT(t) FROM Trip t GROUP BY t.destinationCountry")
    List<Object[]> countTripsByCountry();
    
    /* Compte le nombre de voyages par ville*/
    @Query("SELECT t.destinationCity, COUNT(t) FROM Trip t GROUP BY t.destinationCity")
    List<Object[]> countTripsByCity();
    
    /*Trouve les voyages dans une gamme de prix*/
    List<Trip> findByUnitPriceBetween(int minPrice, int maxPrice);
    
    /* Trouve les voyages les plus populaires (avec le plus de commandes)     */
    @Query("SELECT t FROM Trip t LEFT JOIN t.orders o GROUP BY t ORDER BY COUNT(o) DESC")
    List<Trip> findMostPopularTrips();
    
    /*Trouve les voyages par description (recherche textuelle)*/
    @Query("SELECT t FROM Trip t WHERE LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Trip> findByDescriptionContaining(@Param("keyword") String keyword);
    
    /*Trouve les continents ayant des voyages disponibles*/
    @Query("SELECT DISTINCT t.destinationContinent FROM Trip t")
    List<Continent> findAvailableContinents();
    
    /* Trouve les pays ayant des voyages disponibles pour un continent */
    @Query("SELECT DISTINCT t.destinationCountry FROM Trip t WHERE t.destinationContinent = :continent")
    List<Country> findAvailableCountriesByContinent(@Param("continent") Continent continent);
    
    /* Trouve les villes ayant des voyages disponibles pour un pays  */
    @Query("SELECT DISTINCT t.destinationCity FROM Trip t WHERE t.destinationCountry = :country")
    List<City> findAvailableCitiesByCountry(@Param("country") Country country);
    
    /* Trouve les voyages avec leurs options (fetch join pour éviter N+1)  */
    @Query("SELECT DISTINCT t FROM Trip t LEFT JOIN FETCH t.packageOptions WHERE t.id = :id")
    Trip findByIdWithOptions(@Param("id") Integer id);
    
    /* Trouve tous les voyages avec leurs options */
    @Query("SELECT DISTINCT t FROM Trip t LEFT JOIN FETCH t.packageOptions")
    List<Trip> findAllWithOptions();
    
    /*** Trouve les voyages favoris d'un utilisateur*/
    @Query("SELECT t FROM Trip t JOIN t.lovers u WHERE u.userId = :userId")
    List<Trip> findFavoritesByUserId(@Param("userId") Integer userId);
    
    /*** Recherche avancée avec plusieurs critères optionnels*/
    @Query("SELECT t FROM Trip t WHERE " +
           "(:continent IS NULL OR t.destinationContinent = :continent) AND " +
           "(:country IS NULL OR t.destinationCountry = :country) AND " +
           "(:city IS NULL OR t.destinationCity = :city) AND " +
           "(:minPrice IS NULL OR t.unitPrice >= :minPrice) AND " +
           "(:maxPrice IS NULL OR t.unitPrice <= :maxPrice)")
    List<Trip> findByAdvancedCriteria(
        @Param("continent") Continent continent,
        @Param("country") Country country,
        @Param("city") City city,
        @Param("minPrice") Integer minPrice,
        @Param("maxPrice") Integer maxPrice
    );

}