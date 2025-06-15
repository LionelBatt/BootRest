package com.app.travel.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.travel.dto.ApiResponse;
import com.app.travel.model.Category;
import com.app.travel.model.Option;
import com.app.travel.service.OptionService;

@CrossOrigin
@RestController
@RequestMapping("/options")
public class OptionController {

    @Autowired
    private OptionService optionService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<Option>>> getAllOptions() {
        try {
            List<Option> options = optionService.getAllOptions();
            return ResponseEntity.ok(ApiResponse.success("Options :", options));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la récupération des options"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Option>> getOptionById(@PathVariable int id) {
        try {
            Optional<Option> option = optionService.getOptionById(id);
            if (option.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success("Option :", option.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Option non disponible: " + id));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la récupération de l'option"));
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<Option>>> getOptionsByCategory(@PathVariable String category) {
        try {
            Category categoryEnum = Category.valueOf(category.toUpperCase());
            List<Option> options = optionService.getOptionsByCategory(categoryEnum);
            return ResponseEntity.ok(ApiResponse.success("Options: " + category, options));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Catégorie invalide ou non trouvée: " + category));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la recherche par catégorie"));
        }
    }

    @GetMapping("/price/{minPrice}/{maxPrice}")
    public ResponseEntity<ApiResponse<List<Option>>> getOptionsByPriceRange(
            @PathVariable double minPrice, @PathVariable double maxPrice) {
        
        if (minPrice < 0 || maxPrice < 0 || minPrice > maxPrice) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("min max invalide : " + minPrice + " - " + maxPrice));
        }
        
        try {
            List<Option> options = optionService.getOptionsByPriceRange(minPrice, maxPrice);
            return ResponseEntity.ok(ApiResponse.success("Options entre : " + minPrice + "€ - " + maxPrice + "€", options));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la recherche par prix"));
        }
    }
}