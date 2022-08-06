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

import static com.github.vitaly1983g.restaurants.util.validation.ValidationUtil.assureMenuDataConsistent;
import static com.github.vitaly1983g.restaurants.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminMenuRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class AdminMenuRestaurantController extends AbstractMenuController {
    static final String REST_URL = "/api/admin/restaurants";

    @Autowired
    protected MenuService service;

    @Autowired
    protected RestaurantRepository restaurantRepository;

    @Autowired
    private final MenuRepository menuRepository;

    @GetMapping("/{restId}/menus/{id}")
    public ResponseEntity<Menu> getByDate(@PathVariable int restId, @PathVariable int id,
                                          @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
        return super.getByDate(id, restId, menuDate);
    }

    @GetMapping("/with-menu-on-date")
    public List<Menu> getByDateAllRestaurants(
            @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
        return super.getByDateAllRestaurants(menuDate);
    }

    @GetMapping("/{restId}/menus")
    public List<Menu> getAll(@PathVariable int restId) {
        log.info("getAll menus of restaurant {}", restId);
        return menuRepository.getAll(restId);
    }

    @Transactional
    @DeleteMapping("/{restId}/menus/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByDate(@PathVariable int restId, @PathVariable int id,
                             @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
        log.info("delete menu on date {} of restaurant {}", menuDate, restId);
        menuRepository.delete(menuRepository.checkBelong(id, menuDate, restId));
    }

    @Transactional
    @PutMapping(value = "/{restId}/menus/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody MenuTo menuTo, @PathVariable int restId, @PathVariable int id,
                       @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
        log.info("update menu on date={} of restaurant {}", menuDate, restId);
        assureMenuDataConsistent(menuTo, menuDate, restId);
        //assureIdConsistent(dish, id);
        menuRepository.delete(menuRepository.checkBelong(id, menuDate, restId));
        //menuTo.setId(id);
        service.save(menuTo, restId, menuDate);
    }

    @PostMapping(value = "/{restId}/menus", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Menu> createWithLocation(@Valid @RequestBody MenuTo menuTo, @PathVariable int restId,
                                                   @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
        log.info("create menu {} for restaurant {}", menuTo, restId);
        checkNew(menuTo);
        Menu saved = service.save(menuTo, restId, menuDate);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(saved.getRestaurant().getId(), saved.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(saved);
    }
}