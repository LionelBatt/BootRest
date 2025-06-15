package com.app.travel.controller;

import java.util.List;
import java.util.Optional;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestParam; 
import org.springframework.web.bind.annotation.RestController;

import com.app.travel.dto.ApiResponse;
import com.app.travel.model.City;
import com.app.travel.model.Continent;
import com.app.travel.model.Country;
import com.app.travel.model.Trip;
import com.app.travel.repos.TripRepository;

@CrossOrigin
@RestController
@RequestMapping("/trips")
public class TripController {

    @Autowired
    private TripRepository tripRepository;

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<Trip>>> findAll() {
        try {
            List<Trip> trips = tripRepository.findAll();
            return ResponseEntity.ok(ApiResponse.success("Voyages récupérés avec succès", trips));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la récupération des voyages", e.getMessage()));
        }
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<Trip>> create(@RequestBody Trip trip) {
        try {
            Trip savedTrip = tripRepository.save(trip);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Voyage créé avec succès", savedTrip));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Erreur lors de la création du voyage", e.getMessage()));
        }
    }

    @PutMapping("")
    public ResponseEntity<ApiResponse<Trip>> update(@RequestBody Trip trip) {
        try {
            if (trip.getId() == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("ID du voyage requis pour la mise à jour"));
            }
            
            Optional<Trip> existingTrip = tripRepository.findById(trip.getId());
            if (existingTrip.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Voyage non trouvé avec l'ID: " + trip.getId()));
            }

            Trip updatedTrip = tripRepository.save(trip);
            return ResponseEntity.ok(ApiResponse.success("Voyage mis à jour avec succès", updatedTrip));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la mise à jour du voyage", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Trip>> findById(@PathVariable int id) {
        try {
            Optional<Trip> trip = tripRepository.findById(id);
            if (trip.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success("Voyage trouvé", trip.get()));
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
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable int id) {
        try {
            Optional<Trip> existingTrip = tripRepository.findById(id);
            if (existingTrip.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Voyage non trouvé avec l'ID: " + id));
            }
            
            tripRepository.deleteById(id);
            return ResponseEntity.ok(ApiResponse.success("Voyage supprimé avec succès"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la suppression du voyage", e.getMessage()));
        }
    }

    @GetMapping("/continentdestination/{destination}")
    public ResponseEntity<ApiResponse<List<Trip>>> findByDestination_Continent(@PathVariable String destination) {
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

    @GetMapping("/citydestination/{destination}")
    public ResponseEntity<ApiResponse<List<Trip>>> findByDestination_City(@PathVariable String destination) {
        try {
            City destinationEnum = City.valueOf(destination.toUpperCase());
            List<Trip> trips = tripRepository.findByDestinationCity(destinationEnum);
            return ResponseEntity.ok(ApiResponse.success("Voyages trouvés pour la destination: " + destination, trips));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Destination invalide: " + destination + ". Destinations disponibles: PARIS, LONDON"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la recherche par destination", e.getMessage()));
        }
    }

    @GetMapping("/countrydestination/{destination}")
    public ResponseEntity<ApiResponse<List<Trip>>> findByDestination_Country(@PathVariable String destination) {
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
    public ResponseEntity<ApiResponse<List<Trip>>> findByUserId(@PathVariable Integer userId) {
        try {
            List<Trip> trips = tripRepository.findByUser_UserId(userId);
            return ResponseEntity.ok(ApiResponse.success("Voyages trouvés pour l'utilisateur: " + userId, trips));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la recherche par utilisateur", e.getMessage()));
        }
    }

   /*** Endpoint pour récupérer tous les continents disponibles*/
    @GetMapping("/api/continents")
    public ResponseEntity<ApiResponse<List<String>>> getContinents() {
        try {
            List<String> continents = Arrays.stream(Continent.values())
                    .map(Enum::name)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.success("Continents récupérés avec succès", continents));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la récupération des continents", e.getMessage()));
        }
    }

    /*** Endpoint pour récupérer les pays d'un continent donné   */
    @GetMapping("/api/countries")
    public ResponseEntity<ApiResponse<List<String>>> getCountriesByContinent(
            @RequestParam String continent) {
        try {
            Continent continentEnum = Continent.valueOf(continent.toUpperCase());
            
            // Récupère tous les voyages du continent puis extrait les pays uniques
            List<String> countries = tripRepository.findByDestinationContinent(continentEnum)
                    .stream()
                    .map(trip -> trip.getDestinationCountry().name())
                    .distinct()
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponse.success(
                "Pays récupérés avec succès pour le continent: " + continent, countries));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Continent invalide: " + continent));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la récupération des pays", e.getMessage()));
        }
    }

    /** Endpoint pour récupérer les villes d'un pays donné*/
    @GetMapping("/api/cities")
    public ResponseEntity<ApiResponse<List<String>>> getCitiesByCountry(
            @RequestParam String country) {
        try {
            Country countryEnum = Country.valueOf(country.toUpperCase());
            
            // Récupère tous les voyages du pays puis extrait les villes uniques
            List<String> cities = tripRepository.findByDestinationCountry(countryEnum)
                    .stream()
                    .map(trip -> trip.getDestinationCity().name())
                    .distinct()
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponse.success(
                "Villes récupérées avec succès pour le pays: " + country, cities));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Pays invalide: " + country));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la récupération des villes", e.getMessage()));
        }
    }

    /*** Endpoint pour récupérer les voyages par localisation hiérarchique    */
    @GetMapping("/api/trips-by-location")
    public ResponseEntity<ApiResponse<List<Trip>>> getTripsByLocation(
            @RequestParam(required = false) String continent,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String city) {
        
        try {
            List<Trip> trips;
            String message;

            if (city != null && country != null && continent != null) {
                // Recherche par ville, pays et continent
                City cityEnum = City.valueOf(city.toUpperCase());
                Country countryEnum = Country.valueOf(country.toUpperCase());
                Continent continentEnum = Continent.valueOf(continent.toUpperCase());
                
                trips = tripRepository.findByDestinationContinentAndDestinationCountryAndDestinationCity(
                    continentEnum, countryEnum, cityEnum);
                message = "Voyages trouvés pour " + city + ", " + country + ", " + continent;
                
            } else if (country != null && continent != null) {
                // Recherche par pays et continent
                Country countryEnum = Country.valueOf(country.toUpperCase());
                Continent continentEnum = Continent.valueOf(continent.toUpperCase());
                
                trips = tripRepository.findByDestinationContinentAndDestinationCountry(
                    continentEnum, countryEnum);
                message = "Voyages trouvés pour " + country + ", " + continent;
                
            } else if (continent != null) {
                // Recherche par continent uniquement
                Continent continentEnum = Continent.valueOf(continent.toUpperCase());
                trips = tripRepository.findByDestinationContinent(continentEnum);
                message = "Voyages trouvés pour le continent: " + continent;
                
            } else {
                // Tous les voyages
                trips = tripRepository.findAll();
                message = "Tous les voyages récupérés";
            }
            
            return ResponseEntity.ok(ApiResponse.success(message, trips));
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Paramètre de localisation invalide"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la recherche par localisation", e.getMessage()));
        }
    }

    /*** Endpoint pour obtenir les statistiques par continent */
    @GetMapping("/api/stats/continents")
    public ResponseEntity<ApiResponse<Object>> getContinentStats() {
        try {
            List<Object[]> stats = tripRepository.countTripsByContinent();
            return ResponseEntity.ok(ApiResponse.success("Statistiques par continent récupérées", stats));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la récupération des statistiques", e.getMessage()));
        }
    }

}

