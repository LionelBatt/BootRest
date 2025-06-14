package com.app.travel.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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
import com.app.travel.repos.OptionRepository;

@CrossOrigin
@RestController
@RequestMapping("/options")
public class OptionController {

    @Autowired
    private OptionRepository optionRepository;

    @GetMapping("")
    @Cacheable(value = "options", key = "'all'")
    public ResponseEntity<ApiResponse<List<Option>>> getAllOptions() {
        List<Option> options = optionRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success("Options :", options));
    }

    @GetMapping("/{id}")
    @Cacheable(value = "option-details", key = "#id")
    public ResponseEntity<ApiResponse<Option>> getOptionById(@PathVariable int id) {
        Optional<Option> option = optionRepository.findById(id);
        if (option.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success("Option :", option.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Option non disponible: " + id));
        }
    }

    @GetMapping("/category/{category}")
    @Cacheable(value = "options-by-category", key = "#category")
    public ResponseEntity<ApiResponse<List<Option>>> getOptionsByCategory(@PathVariable String category) {
        try {
            Category categoryEnum = Category.valueOf(category.toUpperCase());
            List<Option> options = optionRepository.findByCategory(categoryEnum);
            return ResponseEntity.ok(ApiResponse.success("Options: " + category, options));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Catégorie invalide ou non trouvée: " + category));
        }
    }


    @GetMapping("/price/{minPrice}/{maxPrice}")
    @Cacheable(value = "options-by-price", key = "#minPrice + ':' + #maxPrice")
    public ResponseEntity<ApiResponse<List<Option>>> getOptionsByPriceRange(
            @PathVariable double minPrice, @PathVariable double maxPrice) {
        
        if (minPrice < 0 || maxPrice < 0 || minPrice > maxPrice) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("min max invalide : " + minPrice + " - " + maxPrice));
        }
        
        List<Option> options = optionRepository.findByPrixBetween(minPrice, maxPrice);
        return ResponseEntity.ok(ApiResponse.success("Options entre : " + minPrice + "€ - " + maxPrice + "€", options));
    }
}