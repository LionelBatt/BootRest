package com.app.travel.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.travel.dto.ApiResponse;
import com.app.travel.model.City;
import com.app.travel.model.Continent;
import com.app.travel.model.Country;
import com.app.travel.model.Trip;
import com.app.travel.repos.TripRepository;
import com.app.travel.utils.ContextUtil;


@CrossOrigin
@RestController
@RequestMapping("/trips")
public class TripController {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private ContextUtil contextUtil;

    @GetMapping("")
    @Cacheable(value = "trips", key = "'all'")
    public ResponseEntity<ApiResponse<List<Trip>>> findAll() {
        try {
            List<Trip> trips = tripRepository.findAll();
            return ResponseEntity.ok(ApiResponse.success("Voyages:", trips));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la récupération des voyages", e.getMessage()));
        }
    }

    @PostMapping("")
    @Caching(evict = { 
        @CacheEvict(value = "trips", allEntries = true),
        @CacheEvict(value = "trip-search", allEntries = true),
        @CacheEvict(value = "destinations", allEntries = true)
    })
    public ResponseEntity<ApiResponse<Trip>> create(@RequestBody Trip trip) {
        try {
            if (!contextUtil.isAdmin()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("Cette ressource n'est pas accessible"));
            }
            Trip createdTrip = tripRepository.save(trip);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Voyage créé avec succès", createdTrip));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Erreur lors de la création du voyage", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @Caching(evict = {
        @CacheEvict(value = "trips", allEntries = true),
        @CacheEvict(value = "trip-details", key = "#id"),
        @CacheEvict(value = "trip-search", allEntries = true),
        @CacheEvict(value = "destinations", allEntries = true)
    })
    public ResponseEntity<ApiResponse<Trip>> update(@PathVariable int id, @RequestBody Trip trip) {
        try {
            if (!contextUtil.isAdmin()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Cette ressource n'est pas accessible"));
            }
            
            Optional<Trip> existingTripOpt = tripRepository.findById(id);
            if (existingTripOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Voyage non trouvé avec l'ID: " + id));
            }
            
            trip.setId(id);
            Trip updatedTrip = tripRepository.save(trip);
            return ResponseEntity.ok(ApiResponse.success("Voyage mis à jour avec succès", updatedTrip));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la mise à jour du voyage", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @Cacheable(value = "trip-details", key = "#id")
    public ResponseEntity<ApiResponse<Trip>> findById(@PathVariable int id) {
        try {
            Optional<Trip> trip = tripRepository.findById(id);
            if (trip.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success("Voyage:", trip.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Voyage non trouvé avec l'ID: " + id));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur lors de la recherche du voyage", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Caching(evict = {
        @CacheEvict(value = "trips", allEntries = true),
        @CacheEvict(value = "trip-details", key = "#id"),
        @CacheEvict(value = "trip-search", allEntries = true),
        @CacheEvict(value = "destinations", allEntries = true)
    })
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable int id) {
        try {
            if (!contextUtil.isAdmin()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("Cette ressource n'est pas accessible"));
            }
            Optional<Trip> existingTrip = tripRepository.findById(id);
            if (existingTrip.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Voyage: " + id));
            }         
            tripRepository.deleteById(id);
            return ResponseEntity.ok(ApiResponse.success("Voyage supprimé avec succès"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur lors de la suppression du voyage", e.getMessage()));
        }
    }

    @GetMapping("/continent/{destination}")
    @Cacheable(value = "destinations", key = "'continent:' + #destination")
    public ResponseEntity<ApiResponse<List<Trip>>> findByDestinationContinent(@PathVariable String destination) {
        try {
            Continent destinationEnum = Continent.valueOf(destination.toUpperCase());
            List<Trip> trips = tripRepository.findByDestinationContinent(destinationEnum);
            return ResponseEntity.ok(ApiResponse.success("Voyages trouvés avec comme continent de destination: " + destination, trips));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Continent de destination invalide: " + destination + "."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la recherche par continent de destination", e.getMessage()));
        }
    }

    @GetMapping("/city/{destination}")
    @Cacheable(value = "destinations", key = "'city:' + #destination")
    public ResponseEntity<ApiResponse<List<Trip>>> findByDestinationCity(@PathVariable String destination) {
        try {
            City destinationEnum = City.valueOf(destination.toUpperCase());
            List<Trip> trips = tripRepository.findByDestinationCity(destinationEnum);
            return ResponseEntity.ok(ApiResponse.success("Voyages trouvés pour la Ville: " + destination, trips));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Destination invalide: " + destination + ". Destinations disponibles: PARIS, LONDON"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la recherche par destination", e.getMessage()));
        }
    }

    @GetMapping("/country/{destination}")
    @Cacheable(value = "destinations", key = "'country:' + #destination")
    public ResponseEntity<ApiResponse<List<Trip>>> findByDestinationCountry(@PathVariable String destination) {
        try {
            Country destinationEnum = Country.valueOf(destination.toUpperCase());
            List<Trip> trips = tripRepository.findByDestinationCountry(destinationEnum);
            return ResponseEntity.ok(ApiResponse.success("Voyages trouvés avec comme pays de destination: " + destination, trips));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Pays de destination invalide: " + destination + "."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la recherche par pays de destination", e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    @Cacheable(value = "destinations", key = "'user:' + #userId")
    public ResponseEntity<ApiResponse<List<Trip>>> findByUserId(@PathVariable Integer userId) {
        try {
            if (!contextUtil.canAccessUser(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Cette ressource n'est pas accessible"));
            }
            List<Trip> trips = tripRepository.findByUser_UserId(userId);
            return ResponseEntity.ok(ApiResponse.success("Voyages de l'utilisateur: " + userId, trips));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la recherche par utilisateur", e.getMessage()));
        }
    }

    // recherche sans filtre : /search/nul/nul/nul/0/9999/0/0/0/0/9999999
    @GetMapping("/search/{destinationContinent}/{destinationCountry}/{destinationCity}/{minimumDuration}/{maximumDuration}/{option1id}/{option2id}/{option3id}/{prixmin}/{prixmax}")
    @Cacheable(value = "trip-search", key = "#destinationContinent + ':' + #destinationCountry + ':' + #destinationCity + ':' + #minimumDuration + ':' + #maximumDuration + ':' + #option1id + ':' + #option2id + ':' + #option3id + ':' + #prixmin + ':' + #prixmax")
    public ResponseEntity<ApiResponse<List<Trip>>> findBasedOnFilter(@PathVariable String destinationContinent, @PathVariable String destinationCountry,@PathVariable String destinationCity, 
    @PathVariable int minimumDuration, @PathVariable int maximumDuration, @PathVariable int option1id, @PathVariable int option2id, @PathVariable int option3id, @PathVariable int prixmin, 
    @PathVariable int prixmax) {
        try {

            Continent destinationCont = destinationContinent.equalsIgnoreCase("nul")? null: Continent.valueOf(destinationContinent.toUpperCase());
            Country destinationCount = destinationCountry.equalsIgnoreCase("nul")? null: Country.valueOf(destinationCountry.toUpperCase());
            City destinationCit = destinationCity.equalsIgnoreCase("nul")? null: City.valueOf(destinationCity.toUpperCase());
            List<Trip> trips = tripRepository.findByDestinationCity(destinationCont, destinationCount, destinationCit, minimumDuration, maximumDuration, option1id, option2id, option3id, prixmin, prixmax);
            return ResponseEntity.ok(ApiResponse.success("Voyages trouvés pour cette recherche: " , trips));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Destination invalide: " + destinationContinent + " / " + destinationCountry + " / " + destinationCity + "."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la recherche", e.getMessage()));
        }
    }
}