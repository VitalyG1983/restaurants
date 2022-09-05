package com.github.vitalydev.restaurants.web.restaurant;

import com.github.vitalydev.restaurants.model.Restaurant;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
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