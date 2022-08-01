package com.github.vitaly1983g.restaurants.web.menu;

import com.github.vitaly1983g.restaurants.repository.RestaurantRepository;
import com.github.vitaly1983g.restaurants.to.MenuTo;
import com.github.vitaly1983g.restaurants.model.Menu;
import com.github.vitaly1983g.restaurants.service.MenuService;
import com.github.vitaly1983g.restaurants.util.MenuUtil;
import com.github.vitaly1983g.restaurants.util.validation.ValidationUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

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

    @GetMapping("/{restId}/menus/{menuDate}")
    public ResponseEntity<MenuTo> getByDate(@PathVariable int restId,
                                            @PathVariable @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
       return super.getByDate(restId, menuDate);
    }

    @GetMapping("/with-menu-on-date")
    public List<MenuTo> getByDateAllRestaurants(
            @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
        return super.getByDateAllRestaurants(menuDate);
    }

    @GetMapping("/{restId}/menus")
    public List<MenuTo> getAll(@PathVariable int restId) {
        log.info("getAll menus of restaurants {}", restId);
        return service.getAll(restId);
    }

    @DeleteMapping("/{restId}/menus/{menuDate}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByDate(@PathVariable int restId,
                             @PathVariable @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
        log.info("delete menu on date {} of restaurants {}", menuDate, restId);
        List<Menu> menu = menuRepository.checkBelong(menuDate, restId);
        menuRepository.deleteAll(menu);
    }

    @PutMapping(value = "/{restId}/menus/{menuDate}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody MenuTo menuTo, @PathVariable int restId,
                       @PathVariable @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
        log.info("update menu on date={} of restaurants {}", menuDate, restId);
        ValidationUtil.assureMenuDataConsistent(menuTo, menuDate, restId);
        menuRepository.checkBelong(menuDate, restId);
        service.save(menuTo, restId, menuDate);
    }

    @PostMapping(value = "/{restId}/menus", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MenuTo> createWithLocation(@Valid @RequestBody MenuTo menuTo, @PathVariable int restId,
                                                     @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
        log.info("create menu {} for restaurants {}", menuTo, restId);
        ValidationUtil.checkNewMenu(menuTo, menuDate, restId, menuRepository);
        List<Menu> saved = service.save(menuTo, restId, menuDate);
        List<MenuTo> savedTos = MenuUtil.getMenuTosByDateForRestaurants(saved, menuDate);
        MenuTo savedTo = savedTos.get(0);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{menuDate}")
                .buildAndExpand(savedTo.getRestaurant().id(), savedTo.getMenuDate()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(savedTo);
    }
}