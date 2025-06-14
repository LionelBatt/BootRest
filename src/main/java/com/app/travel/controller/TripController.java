package com.app.travel.controller;

import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RestController;

import com.app.travel.dto.ApiResponse;
import com.app.travel.model.City;
import com.app.travel.model.Continent;
import com.app.travel.model.Country;
import com.app.travel.model.Trip;
import com.app.travel.repos.TripRepository;
import com.app.travel.service.TripService;
import com.app.travel.utils.ContextUtil;

@CrossOrigin
@RestController
@RequestMapping("/trips")
public class TripController {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private TripService tripService;

    @Autowired
    private ContextUtil contextUtil;

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<Trip>>> findAll() {
        try {
            List<Trip> trips = tripService.getAllTrips();
            return ResponseEntity.ok(ApiResponse.success("Voyages:", trips));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la récupération des voyages"));
        }
    }

    @PostMapping("")
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
                .body(ApiResponse.error("Erreur lors de la création du voyage"));
        }
    }

    @PutMapping("/{id}")
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
                    .body(ApiResponse.error("Erreur lors de la mise à jour du voyage"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Trip>> findById(@PathVariable int id) {
        try {
            Optional<Trip> trip = tripService.getTripById(id);
            if (trip.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success("Voyage:", trip.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Voyage non trouvé avec l'ID: " + id));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Erreur lors de la recherche du voyage"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable int id) {
        try {
            if (!contextUtil.isAdmin()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("Cette ressource n'est pas accessible"));
            }
            Optional<Trip> existingTrip = tripRepository.findById(id);
            if (!existingTrip.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Voyage non trouvé avec l'ID: " + id));
            }         
            tripRepository.deleteById(id);
            return ResponseEntity.ok(ApiResponse.success("Voyage supprimé avec succès"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la suppression du voyage"));
        }
    }

    @GetMapping("/continent/{destination}")
    public ResponseEntity<ApiResponse<List<Trip>>> findByDestinationContinent(@PathVariable String destination) {
        try {
            Continent destinationEnum = Continent.valueOf(destination.toUpperCase());
            List<Trip> trips = tripService.getTripsByContinent(destinationEnum);
            return ResponseEntity.ok(ApiResponse.success("Voyages trouvés avec comme continent de destination: " + destination, trips));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Continent de destination invalide: " + destination + "."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la recherche par continent de destination"));
        }
    }

    @GetMapping("/city/{destination}")
    public ResponseEntity<ApiResponse<List<Trip>>> findByDestinationCity(@PathVariable String destination) {
        try {
            City destinationEnum = City.valueOf(destination.toUpperCase());
            List<Trip> trips = tripService.getTripsByCity(destinationEnum);
            return ResponseEntity.ok(ApiResponse.success("Voyages trouvés pour la Ville: " + destination, trips));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Destination invalide: " + destination + ". Destinations disponibles: PARIS, LONDON"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la recherche par destination"));
        }
    }

    @GetMapping("/country/{destination}")
    public ResponseEntity<ApiResponse<List<Trip>>> findByDestinationCountry(@PathVariable String destination) {
        try {
            Country destinationEnum = Country.valueOf(destination.toUpperCase());
            List<Trip> trips = tripService.getTripsByCountry(destinationEnum);
            return ResponseEntity.ok(ApiResponse.success("Voyages trouvés avec comme pays de destination: " + destination, trips));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Pays de destination invalide: " + destination + "."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la recherche par pays de destination"));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Trip>>> findByUserId(@PathVariable Integer userId) {
        try {
            if (!contextUtil.canAccessUser(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Cette ressource n'est pas accessible"));
            }
            List<Trip> trips = tripService.getTripsByUser(userId);
            return ResponseEntity.ok(ApiResponse.success("Voyages de l'utilisateur: " + userId, trips));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la recherche par utilisateur"));
        }
    }

    /**
     * Offer filter option on trip research in sql database.
     * <p>Without filter research url : /research/nul/nul/nul/0/9999/0/0/0/0/9999999
     * @param destinationContinent (default: "nul")
     * @param destinationCountry (default: "nul")
     * @param destinationCity (default: "nul")
     * @param minimumDuration (default: 0)
     * @param maximumDuration (default: 9999)
     * @param option1id (default: 0)
     * @param option2id (default: 0)
     * @param option3id (default: 0)
     * @param prixmin (default: 0)
     * @param prixmax (default: 9999999)
     * @return List<Trip> of Trip matching research filter
     */
    @GetMapping("/search/{destinationContinent}/{destinationCountry}/{destinationCity}/{minimumDuration}/{maximumDuration}/{option1id}/{option2id}/{option3id}/{prixmin}/{prixmax}")
    public ResponseEntity<ApiResponse<List<Trip>>> findBasedOnFilter(@PathVariable String destinationContinent, @PathVariable String destinationCountry,@PathVariable String destinationCity, 
    @PathVariable int minimumDuration, @PathVariable int maximumDuration, @PathVariable int option1id, @PathVariable int option2id, @PathVariable int option3id, @PathVariable int prixmin, 
    @PathVariable int prixmax) {
        try {
            Continent destinationCont = destinationContinent.equalsIgnoreCase("null")? null: Continent.valueOf(destinationContinent.toUpperCase());
            Country destinationCount = destinationCountry.equalsIgnoreCase("null")? null: Country.valueOf(destinationCountry.toUpperCase());
            City destinationCit = destinationCity.equalsIgnoreCase("null")? null: City.valueOf(destinationCity.toUpperCase());
            Integer opt1 = (option1id == 0)? null: option1id;
            Integer opt2 = (option2id == 0)? null: option2id;
            Integer opt3 = (option3id == 0)? null: option3id;
            List<Trip> trips = tripService.searchTripsWithFilter(destinationCont, destinationCount, destinationCit, minimumDuration, maximumDuration, opt1, opt2, opt3, prixmin, prixmax);
            return ResponseEntity.ok(ApiResponse.success("Voyages trouvés pour cette recherche: " , trips));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Destination invalide: " + destinationContinent + " / " + destinationCountry + " / " + destinationCity + "."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la recherche"));
        }
    }

    @GetMapping("/search/{character}")
    public ResponseEntity<ApiResponse<List<Trip>>> findByCharacter(@PathVariable String character) {
        try {
            List<Trip> trips = tripService.searchTripsByCharacter(character);
            return ResponseEntity.ok(ApiResponse.success("Voyages trouvés contenant le caractère: " + character, trips));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la recherche par caractère"));
        }
    }
}