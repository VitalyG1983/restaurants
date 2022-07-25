package com.github.vitaly1983g.restaurants.web.dish;

import com.github.vitaly1983g.restaurants.model.Menu;
import com.github.vitaly1983g.restaurants.repository.MenuRepository;
import com.github.vitaly1983g.restaurants.service.MenuService;
import com.github.vitaly1983g.restaurants.to.MenuTo;
import com.github.vitaly1983g.restaurants.util.MenuUtil;
import com.github.vitaly1983g.restaurants.util.validation.ValidationUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class AdminMenuRestaurantController {
    static final String REST_URL = "/api/admin/restaurants/{restId}/menus";

    private final MenuRepository repository;
    private final MenuService service;

    //нет смысла получать 1 строку из таблицы меню(тк в этой строке будет 1 dish, а не все)
/*    @GetMapping("/{menuId}")
    public ResponseEntity<Menu> get(@PathVariable int menuId, @PathVariable int restId) {
        log.info("get menu with id={} of restaurant {}", menuId, restId);
        return ResponseEntity.of(repository.get(menuId, restId));
    }*/

    @GetMapping("/{menuDate}")
    public ResponseEntity<MenuTo> getByDate(@PathVariable int restId,
                                            @PathVariable @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
        log.info("get menu on date {} of restaurant {}", menuDate, restId);
        List<Menu> menus = repository.getByDate(menuDate, restId);
        return ResponseEntity.of(MenuUtil.getMenuTos(menus, restId).stream().findFirst());
    }

    @GetMapping
    public List<MenuTo> getAll(@PathVariable int restId) {
        log.info("getAll menus of restaurant {}", restId);
        List<Menu> all = repository.getAll(restId);
        return MenuUtil.getMenuTos(all, restId);
    }

    @DeleteMapping("/{menuDate}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByDate(@PathVariable int restId,
                             @PathVariable @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
        log.info("delete menu on date {} of restaurant {}", menuDate, restId);
        List<Menu> menu = repository.checkBelong(menuDate, restId);
        repository.deleteAll(menu);
    }

    @PutMapping(value = "/{menuDate}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody MenuTo menuTo, @PathVariable int restId,
                       @PathVariable @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
        log.info("update menu on date={} of restaurant {}", menuDate, restId);
        ValidationUtil.assureMenuDateConsistent(menuTo, menuDate, restId, repository);
        repository.checkBelong(menuDate, restId);
        service.save(menuTo, restId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MenuTo> createWithLocation(@Valid @RequestBody MenuTo menuTo, @PathVariable int restId,
                                                     @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
        log.info("create menu {} for restaurant {}", menuTo, restId);
        ValidationUtil.checkNewMenu(menuTo, menuDate, restId, repository);
        List<Menu> saved = service.save(menuTo, restId);
        List<MenuTo> savedTos = MenuUtil.getMenuTos(saved, saved.get(0).getRestId());
        MenuTo savedTo = savedTos.get(0);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{menuDate}")
                .buildAndExpand(savedTo.getRestId(), savedTo.getMenuDate()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(savedTo);
    }
}