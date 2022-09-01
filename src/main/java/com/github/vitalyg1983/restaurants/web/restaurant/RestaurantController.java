package com.github.vitalyg1983.restaurants.web.restaurant;

import com.github.vitalyg1983.restaurants.model.Restaurant;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = RestaurantController.API_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class RestaurantController extends AbstractRestaurantController {
    static final String API_URL = "/api/restaurants";

    @GetMapping("/{restId}")
    public Restaurant get(@PathVariable int restId) {
        return super.get(restId);
    }

    @GetMapping
    public List<Restaurant> getAll() {
        return super.getAll();
    }
}