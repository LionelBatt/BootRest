package com.app.travel.controller;

import com.app.travel.model.Trip;
import com.app.travel.repos.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/trips")
public class TripController {

    @Autowired
    private TripRepository tripRepository;

    @GetMapping("")
    public List<Trip> findAll() {
        return tripRepository.findAll();
    }

    @PostMapping("")
    public void create(@RequestBody Trip trip) {
        tripRepository.save(trip);
    }

    @PutMapping("")
    public void update(@RequestBody Trip trip) {
        tripRepository.save(trip);
    }

    @GetMapping("/{id}")
    public Trip findById(@PathVariable int id) {
        return tripRepository.findById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        tripRepository.deleteById(id);
    }

    @GetMapping("/country/{country}")
    public List<Trip> findByCountry(@PathVariable String country) {
        return tripRepository.findByDestination_Country(country);
    }

    @GetMapping("/continent/{continent}")
    public List<Trip> findByContinent(@PathVariable String continent) {
        return tripRepository.findByDestination_Continent(continent);
    }

    @GetMapping("/city/{city}/one")
    public Trip findOneByCity(@PathVariable String city) {
        return tripRepository.findFirstByDestination_City(city);
}
}