package com.app.travel.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.app.travel.model.City;
import com.app.travel.model.Continent;
import com.app.travel.model.Country;
import com.app.travel.model.Trip;
import com.app.travel.repos.TripRepository;

@Service
public class TripService {

    @Autowired
    private TripRepository tripRepository;

    @Cacheable(value = "trips", key = "'all'")
    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    @Cacheable(value = "destinations", key = "'continent:' + #continent")
    public List<Trip> getTripsByContinent(Continent continent) {
        return tripRepository.findByDestinationContinent(continent);
    }

    @Cacheable(value = "destinations", key = "'country:' + #country")
    public List<Trip> getTripsByCountry(Country country) {
        return tripRepository.findByDestinationCountry(country);
    }

    @Cacheable(value = "destinations", key = "'city:' + #city")
    public List<Trip> getTripsByCity(City city) {
        return tripRepository.findByDestinationCity(city);
    }

    @Cacheable(value = "trip-details", key = "#id")
    public Optional<Trip> getTripById(int id) {
        return tripRepository.findById(id);
    }

    @Cacheable(value = "trip-search", key = "'search:' + #character")
    public List<Trip> searchTripsByCharacter(String character) {
        return tripRepository.findByCharacter(character);
    }

    @Cacheable(value = "trips", key = "'user:' + #userId")
    public List<Trip> getTripsByUser(Integer userId) {
        return tripRepository.findByUser_UserId(userId);
    }

    @Cacheable(value = "trip-search", key = "#destinationContinent + ':' + #destinationCountry + ':' + #destinationCity + ':' + #minimumDuration + ':' + #maximumDuration + ':' + #option1id + ':' + #option2id + ':' + #option3id + ':' + #prixmin + ':' + #prixmax")
    public List<Trip> searchTripsWithFilter(Continent destinationContinent, Country destinationCountry, City destinationCity, 
            int minimumDuration, int maximumDuration, Integer option1id, Integer option2id, Integer option3id, 
            int prixmin, int prixmax) {
        return tripRepository.findByDestinationCityWithOptions(destinationContinent, destinationCountry, destinationCity, 
                minimumDuration, maximumDuration, option1id, option2id, option3id, prixmin, prixmax);
    }
}