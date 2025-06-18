package com.app.travel.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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

    /**
     * Endpoint pour récupérer tous les voyages
     * @return ResponseEntity<ApiResponse<List<Trip>>>
     */
    @GetMapping("")
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<List<Trip>>> findAll() {
        try {
            List<Trip> trips = tripService.getAllTrips();
            return ResponseEntity.ok(ApiResponse.success("Voyages:", trips));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la récupération des voyages"));
        }
    }

    /**
     * Endpoint pour créer un nouveau voyage
     * @param trip
     * @return ResponseEntity<ApiResponse<Trip>>
     */
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
    
    /**
     * Endpoint pour mettre à jour un voyage existant
     * @param id
     * @param trip
     * @return ResponseEntity<ApiResponse<Trip>>
     */
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

    /**
     * Endpoint pour récupérer un voyage par son ID
     * @param id
     * @return ResponseEntity<ApiResponse<Trip>>
     */
    @GetMapping("/{id}")
    @Transactional(readOnly = true)
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

    /**
     * Endpoint pour supprimer un voyage par son ID
     * @param id
     * @return ResponseEntity<ApiResponse<Void>>
     */
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

    /**
     * Endpoint pour récupérer les voyages par continent de destination
     * @param destination
     * @return ResponseEntity<ApiResponse<List<Trip>>>
     */
    @GetMapping("/continent/{destination}")
    @Transactional(readOnly = true)
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
    
    /**
     * Endpoint pour récupérer les voyages par ville de destination
     * @param destination
     * @return ResponseEntity<ApiResponse<List<Trip>>>
     */
    @GetMapping("/city/{destination}")
    @Transactional(readOnly = true)
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
    
    /**
     * Endpoint pour récupérer les voyages par pays de destination
     * @param destination
     * @return ResponseEntity<ApiResponse<List<Trip>>>
     */
    @GetMapping("/country/{destination}")
    @Transactional(readOnly = true)
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
    /**
     * Endpoint pour récupérer les voyages par utilisateur
     * @param userId
     * @return ResponseEntity<ApiResponse<List<Trip>>>
     */
    @GetMapping("/user/{userId}")
    @Transactional(readOnly = true)
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
     * <p>Without filter research url : /filter/null/null/null/0/9999/0/0/0/0/9999999
     * @param destinationContinent (default: "null")
     * @param destinationCountry (default: "null")
     * @param destinationCity (default: "null")
     * @param minimumDuration (default: 0)
     * @param maximumDuration (default: 9999)
     * @param optionsid (default: 0)
     * @param prixmin (default: 0)
     * @param prixmax (default: 9999999)
     * @return List<Trip> of Trip matching research filter
     */
    @GetMapping("/filter/{destinationContinent}/{destinationCountry}/{destinationCity}/{minimumDuration}/{maximumDuration}/{optionsid}/{prixmin}/{prixmax}")
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<List<Trip>>> findBasedOnFilter(@PathVariable String destinationContinent, @PathVariable String destinationCountry,@PathVariable String destinationCity, 
    @PathVariable int minimumDuration, @PathVariable int maximumDuration, @PathVariable String optionsid, @PathVariable int prixmin, @PathVariable int prixmax) {
        try {
            // Validation et conversion des paramètres
            Continent destinationCont = null;
            Country destinationCount = null;
            City destinationCit = null;
            
            // Conversion sécurisée des enums
            if (destinationContinent != null && !destinationContinent.equalsIgnoreCase("null") && !destinationContinent.trim().isEmpty()) {
                try {
                    destinationCont = Continent.valueOf(destinationContinent.toUpperCase());
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("Continent invalide: " + destinationContinent + ". Continents disponibles: " + java.util.Arrays.toString(Continent.values())));
                }
            }
            
            if (destinationCountry != null && !destinationCountry.equalsIgnoreCase("null") && !destinationCountry.trim().isEmpty()) {
                try {
                    destinationCount = Country.valueOf(destinationCountry.toUpperCase());
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("Pays invalide: " + destinationCountry + ". Pays disponibles: " + java.util.Arrays.toString(Country.values())));
                }
            }
            
            if (destinationCity != null && !destinationCity.equalsIgnoreCase("null") && !destinationCity.trim().isEmpty()) {
                try {
                    destinationCit = City.valueOf(destinationCity.toUpperCase());
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("Ville invalide: " + destinationCity + ". Villes disponibles: " + java.util.Arrays.toString(City.values())));
                }
            }
            
            // Validation des paramètres numériques
            if (minimumDuration < 0 || maximumDuration < 0 || prixmin < 0 || prixmax < 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Les valeurs numériques ne peuvent pas être négatives"));
            }
            
            if (minimumDuration > maximumDuration) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("La durée minimale ne peut pas être supérieure à la durée maximale"));
            }
            
            if (prixmin > prixmax) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Le prix minimum ne peut pas être supérieur au prix maximum"));
            }
            
            optionsid = (optionsid.equalsIgnoreCase("-1"))? "": optionsid;

            // Recherche avec les filtres
            List<Trip> trips = tripService.searchTripsWithFilter(
                destinationCont, destinationCount, destinationCit, 
                minimumDuration, maximumDuration, 
                optionsid, 
                prixmin, prixmax
            );
            
            // Message informatif sur les filtres appliqués
            StringBuilder filterInfo = new StringBuilder("Recherche avec filtres: ");
            if (destinationCont != null) filterInfo.append("Continent=").append(destinationCont).append(", ");
            if (destinationCount != null) filterInfo.append("Pays=").append(destinationCount).append(", ");
            if (destinationCit != null) filterInfo.append("Ville=").append(destinationCit).append(", ");
            filterInfo.append("Durée=[").append(minimumDuration).append("-").append(maximumDuration).append("], ");
            filterInfo.append("Prix=[").append(prixmin).append("€-").append(prixmax).append("€]");
            filterInfo.append(", Options=[").append(optionsid).append("]");
                        
            return ResponseEntity.ok(ApiResponse.success(filterInfo.toString() + " - " + trips.size() + " résultat(s)", trips));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Paramètre invalide: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la recherche avec filtres: " + e.getMessage()));
        }
    }

    /**
     * Endpoint pour rechercher des voyages par caractère dans la description, la ville, le pays ou le continent
     * @param character
     * @return ResponseEntity<ApiResponse<List<Trip>>>
     */
    @GetMapping("/search/text/{character}")
    @Transactional(readOnly = true)
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

