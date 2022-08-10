package com.github.vitaly1983g.restaurants.web.menu;

import com.github.vitaly1983g.restaurants.model.Menu;
import com.github.vitaly1983g.restaurants.repository.MenuRepository;
import com.github.vitaly1983g.restaurants.repository.RestaurantRepository;
import com.github.vitaly1983g.restaurants.service.MenuService;
import com.github.vitaly1983g.restaurants.to.MenuTo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class AdminMenuRestaurantController extends AbstractMenuController {
    static final String API_URL = "/api/admin/restaurants/{restId}/menus";

    @Autowired
    protected MenuService service;

    @Autowired
    protected RestaurantRepository restaurantRepository;

    @Autowired
    private final MenuRepository menuRepository;

    @GetMapping(API_URL + "/{id}")
    public ResponseEntity<Menu> get(@PathVariable int restId, @PathVariable int id) {
        return ResponseEntity.of(menuRepository.get(id, restId));
    }

    @GetMapping(API_URL + "/by-date")
    public ResponseEntity<Menu> getByDate(@PathVariable int restId,
                                          @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
        return super.getByDate(restId, menuDate);
    }

    @GetMapping("/api/admin/restaurants/menus/all-by-date")
    public List<Menu> getAllForRestaurantsByDate(
            @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
        return super.getAllForRestaurantsByDate(menuDate);
    }

    @GetMapping(API_URL)
    public List<Menu> getAllForRestaurant(@PathVariable int restId) {
        log.info("getAll menus of restaurant {}", restId);
        return menuRepository.getAllForRestaurant(restId);
    }

    @Transactional
    @DeleteMapping(API_URL + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int restId, @PathVariable int id) {
        log.info("delete menu id={} of restaurant {}", id, restId);
        menuRepository.delete(menuRepository.checkBelong(id, restId));
    }

    @Transactional
    @PutMapping(value = API_URL + "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody MenuTo menuTo, @PathVariable int restId, @PathVariable int id) {
        log.info("update menu id={} of restaurant {}", id, restId);
        Menu menu = menuRepository.checkBelong(id, restId);
        // to avoid: Нарушение уникального индекса или первичного ключа: \"PUBLIC.DISHINMENU_UNIQUE_MENU_ID_DISH_ID_IDX
        // I do before save(menu): menuRepository.deleteExisted(id);
        // But in this case, will be generated new 'id' for saved Menu object
        menuRepository.deleteExisted(id);
        service.save(menuTo, restId, menu.getMenuDate());
    }

    @Transactional
    @PostMapping(value = API_URL, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Menu> createWithLocation(@Valid @RequestBody MenuTo menuTo, @PathVariable int restId,
                                                   @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
        log.info("create menu {} for restaurant {}", menuTo, restId);
        Menu saved = service.save(menuTo, restId, menuDate);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(API_URL + "/{id}")
                .buildAndExpand(saved.getRestaurant().getId(), saved.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(saved);
    }
}