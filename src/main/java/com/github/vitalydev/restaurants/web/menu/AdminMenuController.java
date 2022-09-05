package com.github.vitalydev.restaurants.web.menu;

import com.github.vitalydev.restaurants.model.Menu;
import com.github.vitalydev.restaurants.repository.RestaurantRepository;
import com.github.vitalydev.restaurants.service.MenuService;
import com.github.vitalydev.restaurants.to.MenuTo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static com.github.vitalydev.restaurants.util.validation.ValidationUtil.assureIdConsistent;
import static com.github.vitalydev.restaurants.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@CacheConfig(cacheNames = {"menu", "menus"})
public class AdminMenuController extends AbstractMenuController {
    static final String API_URL = "/api/admin/restaurants/{restId}/menus";

    private MenuService service;
    private RestaurantRepository restaurantRepository;

    @GetMapping(API_URL + "/{id}")
    public ResponseEntity<Menu> get(@PathVariable int restId, @PathVariable int id) {
        restaurantRepository.checkExistence(restId);
        return ResponseEntity.of(menuRepository.get(id, restId));
    }

    @GetMapping(API_URL + "/by-date")
    public ResponseEntity<Menu> getByDate(@PathVariable int restId,
                                          @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
        return super.getByDate(restId, menuDate == null ? LocalDate.now() : menuDate);
    }

    @GetMapping("/api/admin/restaurants/menus/by-date")
    public List<Menu> getAllForRestaurantsByDate(
            @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
        return super.getAllForRestaurantsByDate(menuDate == null ? LocalDate.now() : menuDate);
    }

    @GetMapping(API_URL)
    public List<Menu> getAllForRestaurant(@PathVariable int restId) {
        log.info("getAll menus of restaurant {}", restId);
        restaurantRepository.checkExistence(restId);
        return menuRepository.getAllForRestaurant(restId);
    }

    @Transactional
    @DeleteMapping(API_URL + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void delete(@PathVariable int restId, @PathVariable int id) {
        log.info("delete menu id={} of restaurant {}", id, restId);
        menuRepository.delete(menuRepository.checkBelong(id, restId));
    }

    @PatchMapping(value = API_URL + "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void update(@Valid @RequestBody MenuTo menuTo, @PathVariable int restId, @PathVariable int id) {
        log.info("update menu id={} of restaurant {}", id, restId);
        assureIdConsistent(menuTo, id);
        service.update(menuTo, restId, id);
    }

    @PostMapping(value = API_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(allEntries = true)
    public ResponseEntity<Menu> createWithLocation(@Valid @RequestBody MenuTo menuTo, @PathVariable int restId) {
        log.info("create menu {} for restaurant {}", menuTo, restId);
        checkNew(menuTo);
        Menu saved = service.create(menuTo, restId, menuTo.getMenuDate());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(API_URL + "/{id}")
                .buildAndExpand(saved.getRestaurant().getId(), saved.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(saved);
    }
}