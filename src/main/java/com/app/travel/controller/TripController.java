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
    @GetMapping("/research/{destinationContinent}/{destinationCountry}/{destinationCity}/{minimumDuration}/{maximumDuration}/{option1id}/{option2id}/{option3id}/{prixmin}/{prixmax}")
    public ResponseEntity<ApiResponse<List<Trip>>> findBasedOnFilter(@PathVariable String destinationContinent, @PathVariable String destinationCountry,@PathVariable String destinationCity, 
    @PathVariable int minimumDuration, @PathVariable int maximumDuration, @PathVariable int option1id, @PathVariable int option2id, @PathVariable int option3id, @PathVariable int prixmin, 
    @PathVariable int prixmax) {
        try {

            Continent destinationCont = destinationContinent.equalsIgnoreCase("nul")? null: Continent.valueOf(destinationContinent.toUpperCase());
            Country destinationCount = destinationCountry.equalsIgnoreCase("nul")? null: Country.valueOf(destinationCountry.toUpperCase());
            City destinationCit = destinationCity.equalsIgnoreCase("nul")? null: City.valueOf(destinationCity.toUpperCase());
            int opt1 = (option1id == 0)? null: option1id;
            int opt2 = (option2id == 0)? null: option2id;
            int opt3 = (option3id == 0)? null: option3id;
            List<Trip> trips = tripRepository.findByDestinationCity(destinationCont, destinationCount, destinationCit, minimumDuration, maximumDuration, opt1, opt2, opt3, prixmin, prixmax);
            return ResponseEntity.ok(ApiResponse.success("Voyages trouvés pour cette recherche: " , trips));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Destination invalide: " + destinationContinent + " / " + destinationCountry + " / " + destinationCity + "."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la recherche", e.getMessage()));
        }
    }

    @GetMapping("/search/{character}")
    public ResponseEntity<ApiResponse<List<Trip>>> findByCharacter(@PathVariable String character) {
        try {
            List<Trip> trips = tripRepository.findByCharacter(character);
            return ResponseEntity.ok(ApiResponse.success("Voyages trouvés contenant le caractère: " + character, trips));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la recherche par caractère", e.getMessage()));
        }
    }
}