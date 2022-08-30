package com.github.vitalyg1983.restaurants.web.dish;

import com.github.vitalyg1983.restaurants.model.Dish;
import com.github.vitalyg1983.restaurants.repository.DishRepository;
import com.github.vitalyg1983.restaurants.repository.RestaurantRepository;
import com.github.vitalyg1983.restaurants.util.validation.ValidationUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = AdminDishController.API_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class AdminDishController {
    static final String API_URL = "/api/admin/restaurants/{restId}/dishes";

    private final DishRepository repository;
    private final RestaurantRepository restaurantRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Dish> get(@PathVariable int restId, @PathVariable int id) {
        log.info("get dish {} of restaurant {}", id, restId);
        return ResponseEntity.of(repository.get(id, restId));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int restId, @PathVariable int id) {
        log.info("delete dish {} of restaurant {}", id, restId);
        Dish dish = repository.checkBelong(id, restId);
        repository.delete(dish);
    }

    @GetMapping
    public List<Dish> getAll(@PathVariable int restId) {
        log.info("getAll for restaurant {}", restId);
        return repository.getAll(restId);
    }

    @Transactional
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Dish dish, @PathVariable int id, @PathVariable int restId) {
        log.info("update {} for user {}", dish, restId);
        ValidationUtil.assureIdConsistent(dish, id);
        repository.checkBelong(id, restId);
        dish.setRestId(restId);
        repository.save(dish);
    }

    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@PathVariable int restId, @Valid @RequestBody Dish dish) {
        log.info("create dish {} for restaurant {}", dish, restId);
        ValidationUtil.checkNew(dish);
        restaurantRepository.checkExistence(restId);
        dish.setRestId(restId);
        Dish created = repository.save(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(API_URL + "/{id}")
                .buildAndExpand(created.getRestId(), created.getId())
                .toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}