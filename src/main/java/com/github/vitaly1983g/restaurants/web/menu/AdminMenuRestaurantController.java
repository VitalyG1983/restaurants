package com.github.vitaly1983g.restaurants.web.menu;

import com.github.vitaly1983g.restaurants.model.Menu;
import com.github.vitaly1983g.restaurants.repository.MenuRepository;
import com.github.vitaly1983g.restaurants.repository.RestaurantRepository;
import com.github.vitaly1983g.restaurants.service.MenuService;
import com.github.vitaly1983g.restaurants.to.MenuTo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
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
@RequestMapping(value = AdminMenuRestaurantController.API_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class AdminMenuRestaurantController extends AbstractMenuController {
    static final String API_URL = "/api/admin/restaurants";

    @Autowired
    protected MenuService service;

    @Autowired
    protected RestaurantRepository restaurantRepository;

    @Autowired
    private final MenuRepository menuRepository;

    @GetMapping("/{restId}/menus/{id}")
    public ResponseEntity<Menu> get(@PathVariable int restId, @PathVariable int id) {
        return ResponseEntity.of(menuRepository.get(id, restId));
    }

    @GetMapping("/{restId}/menus/by-date")
    public ResponseEntity<Menu> getByDate(@PathVariable int restId,
                                          @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
        return super.getByDate(restId, menuDate);
    }

    @GetMapping("/menus/all-by-date")
    public List<Menu> allRestaurantsGetByDate(
            @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
        return super.allRestaurantsGetByDate(menuDate);
    }

    @GetMapping("/{restId}/menus")
    public List<Menu> getAll(@PathVariable int restId) {
        log.info("getAll menus of restaurant {}", restId);
        return menuRepository.getAll(restId);
    }

    @Transactional
    @DeleteMapping("/{restId}/menus/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int restId, @PathVariable int id) {
        log.info("delete menu id={} of restaurant {}", id, restId);
        menuRepository.delete(menuRepository.checkBelong(id, restId));
    }

    @Transactional
    //@Modifying(clearAutomatically=true, flushAutomatically=true)
    @PutMapping(value = "/{restId}/menus/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
   public void update(@Valid @RequestBody MenuTo menuTo, @PathVariable int restId, @PathVariable int id){
         log.info("update menu id={} of restaurant {}", id, restId);
       // assureMenuDataConsistent(menuTo, menuDate, restId);
        //assureIdConsistent(dish, id);
        Menu menu = menuRepository.checkBelong(id, restId);
        menuRepository.deleteExisted(id);
        service.save(menuTo, restId, menu.getMenuDate(), id);
    }

    @Transactional
    @PostMapping(value = "/{restId}/menus", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Menu> createWithLocation(@Valid @RequestBody MenuTo menuTo, @PathVariable int restId,
                                                   @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
        log.info("create menu {} for restaurant {}", menuTo, restId);
        //checkNew(menuTo);
        Menu saved = service.save(menuTo, restId, menuDate);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(API_URL + "/{id}")
                .buildAndExpand(saved.getRestaurant().getId(), saved.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(saved);
    }
}