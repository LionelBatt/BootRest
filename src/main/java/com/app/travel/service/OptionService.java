package com.app.travel.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.app.travel.model.Category;
import com.app.travel.model.Option;
import com.app.travel.repos.OptionRepository;

@Service
public class OptionService {

    @Autowired
    private OptionRepository optionRepository;

    @Cacheable(value = "options", key = "'all'")
    public List<Option> getAllOptions() {
        return optionRepository.findAll();
    }

    @Cacheable(value = "option-details", key = "#id")
    public Optional<Option> getOptionById(int id) {
        return optionRepository.findById(id);
    }

    @Cacheable(value = "options-by-category", key = "#category")
    public List<Option> getOptionsByCategory(Category category) {
        return optionRepository.findByCategory(category);
    }

    @Cacheable(value = "options-by-price", key = "#minPrice + ':' + #maxPrice")
    public List<Option> getOptionsByPriceRange(double minPrice, double maxPrice) {
        return optionRepository.findByPrixBetween(minPrice, maxPrice);
    }
}
