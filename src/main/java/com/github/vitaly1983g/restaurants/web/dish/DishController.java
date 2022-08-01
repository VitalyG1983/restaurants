package com.github.vitaly1983g.restaurants.web.dish;

import com.github.vitaly1983g.restaurants.model.Dish;
import com.github.vitaly1983g.restaurants.repository.DishRepository;
import com.github.vitaly1983g.restaurants.util.validation.ValidationUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static com.github.vitaly1983g.restaurants.util.validation.ValidationUtil.assureIdConsistent;

@RestController
@RequestMapping(value = DishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class DishController {
    static final String REST_URL = "/api/admin/restaurants/{restId}/dishes";

    private final DishRepository repository;

    @GetMapping("/{id}")
    public ResponseEntity<Dish> get(@PathVariable int restId, @PathVariable int id) {
        log.info("get dish {} of restaurants {}", id, restId);
        return ResponseEntity.of(repository.get(id, restId));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int restId, @PathVariable int id) {
        log.info("delete dish {} of restaurants {}", id, restId);
        Dish dish = repository.checkBelong(id, restId);
        repository.delete(dish);
    }

    @GetMapping
    public List<Dish> getAll(@PathVariable int restId) {
        log.info("getAll for restaurants {}", restId);
        return repository.getAll(restId);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Dish dish, @PathVariable int id, @PathVariable int restId) {
        log.info("update {} for user {}", dish, restId);
        assureIdConsistent(dish, id);
        repository.checkBelong(id, restId);
        dish.setRestId(restId);
        repository.save(dish);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@PathVariable int restId, @Valid @RequestBody Dish dish) {
        log.info("create dish {} for restaurants {}", dish, restId);
        ValidationUtil.checkNew(dish);
        dish.setRestId(restId);
        Dish created = repository.save(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getRestId(), created.getId())
                .toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

  /*  @GetMapping("/filter")
    public List<MealTo> getBetween(@AuthenticationPrincipal AuthUser authUser,
                                   @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                   @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
                                   @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                   @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {

        int restId = restId;
        log.info("getBetween dates({} - {}) time({} - {}) for user {}", startDate, endDate, startTime, endTime, restId);
        List<Dish> mealsDateFiltered = repository.getBetweenHalfOpen(atStartOfDayOrMin(startDate), atStartOfNextDayOrMax(endDate), restId);
        return MealsUtil.getFilteredTos(mealsDateFiltered, authUser.getUser().getCaloriesPerDay(), startTime, endTime);
    }*/
}