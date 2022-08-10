package com.github.vitaly1983g.restaurants.web.restaurant;

import com.github.vitaly1983g.restaurants.model.Restaurant;
import com.github.vitaly1983g.restaurants.repository.RestaurantRepository;
import com.github.vitaly1983g.restaurants.util.validation.ValidationUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = AdminRestaurantController.API_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@CacheConfig(cacheNames = {"menu", "menus"})
public class AdminRestaurantController {
    static final String API_URL = "/api/admin/restaurants";

    private final RestaurantRepository repository;

    @GetMapping("/{restId}")
    public ResponseEntity<Restaurant> get(@PathVariable int restId) {
        log.info("get restaurant id={}", restId);
        return ResponseEntity.of(repository.findById(restId));
    }

    @GetMapping("/{restId}/with-dishes")
    public ResponseEntity<Restaurant> getWithDishes(@PathVariable int restId) {
        log.info("get restaurant id={} with dishIds", restId);
        return ResponseEntity.of(repository.getWithDishes(restId));
    }

    @GetMapping
    public List<Restaurant> getAll() {
        log.info("getAll restaurants");
        return repository.findAll(Sort.by("name"));
    }

    @DeleteMapping("/{restId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void delete(@PathVariable int restId) {
        log.info("delete restaurant id={}", restId);
        repository.deleteExisted(restId);
    }

    @PutMapping(value = "/{restId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable int restId) {
        log.info("update restaurant {}", restId);
        ValidationUtil.assureIdConsistent(restaurant, restId);
        repository.save(restaurant);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(allEntries = true)
    public ResponseEntity<Restaurant> createWithLocation(@Valid @RequestBody Restaurant restaurant) {
        log.info("create restaurant {}", restaurant);
        ValidationUtil.checkNew(restaurant);
        Restaurant saved = repository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(API_URL + "/{restId}")
                .buildAndExpand(saved.id()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(saved);
    }
}