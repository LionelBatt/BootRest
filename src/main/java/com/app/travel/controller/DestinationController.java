package com.app.travel.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.travel.model.City;
import com.app.travel.model.Continent;
import com.app.travel.model.Country;

@RestController
@RequestMapping("/destination")
public class DestinationController {

    @GetMapping("/continents")
    public List<Map<String, Object>> getContinents() {
        return Arrays.stream(Continent.values())
                .map(c -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", c.getId());
                    map.put("name", c.name());
                    return map;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/countries")
    public List<Map<String, Object>> getCountry() {
        return Arrays.stream(Country.values())
                .map(c -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", c.getId());
                    map.put("name", c.name());
                    return map;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/cities")
    public List<Map<String, Object>> getCity() {
        return Arrays.stream(City.values())
                .map(c -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", c.getId());
                    map.put("name", c.name());
                    return map;
                })
                .collect(Collectors.toList());
    }
}
