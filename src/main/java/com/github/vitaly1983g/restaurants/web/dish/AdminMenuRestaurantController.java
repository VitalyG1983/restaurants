package com.github.vitaly1983g.restaurants.web.dish;

import com.github.vitaly1983g.restaurants.model.Menu;
import com.github.vitaly1983g.restaurants.repository.MenuRepository;
import com.github.vitaly1983g.restaurants.service.MenuService;
import com.github.vitaly1983g.restaurants.to.MenuTo;
import com.github.vitaly1983g.restaurants.util.MenuUtil;
import com.github.vitaly1983g.restaurants.web.AuthUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static com.github.vitaly1983g.restaurants.util.validation.ValidationUtil.assureIdConsistent;
import static com.github.vitaly1983g.restaurants.util.validation.ValidationUtil.checkNew;

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

  /*  @GetMapping("/by-date")
    public ResponseEntity<Menu> getByDate(@PathVariable int restId,
                                          @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
        log.info("get menu on date {} of restaurant {}", menuDate, restId);
         return ResponseEntity.of(repository.getByDate(menuDate, restId));
    }*/

 /*   @GetMapping("/{menuId}")
    public ResponseEntity<Menu> getWithDish(@PathVariable int menuId, @PathVariable int restId) {
        log.info("get menu {} of restaurant {}", menuId, restId);
        return ResponseEntity.of(repository.getWithDish(menuId, restId));
    }*/

    @GetMapping
    public List<MenuTo> getAll(@PathVariable int restId) {
        log.info("getAll menus of restaurant {}", restId);
        List<Menu> all = repository.getAll(restId);
        return MenuUtil.getMenuTos(all, restId);
    }

/*    @DeleteMapping("/{menuId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int menuId, @PathVariable int restId,
                       @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
        log.info("delete menu {} of restaurant {}", menuId, restId);
        Menu menu = repository.checkBelong(menuId, restId);
        //repository.delete(menu);
        repository.deleteAllById(Collections.singleton(menuId));
    }*/

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
    public void update(@Valid @RequestBody Menu menu, @PathVariable int restId,
                       @PathVariable @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
       /* int userId = authUser.id();
        log.info("update menu on date={} of restaurant {}", menuDate, restId);
        assureIdConsistent(menu, id);
        repository.checkBelong(id, userId);*/
       // service.save(menu, userId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Menu>> createWithLocation(@Valid @RequestBody MenuTo menuTo, @PathVariable int restId) {
        //int userId = authUser.id();
        log.info("create menu {} for restaurant {}", menuTo, restId);
        checkNew(menuTo);
        List<Menu> created = service.save(menuTo, restId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{menuDate}")
                .buildAndExpand(menuTo.getMenuDate()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }


/*    @GetMapping("/filter")
    public List<LunchTo> getBetween(@AuthenticationPrincipal AuthUser authUser,
                                    @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                    @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
                                    @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                    @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {

        int userId = authUser.id();
        log.info("getBetween dates({} - {}) time({} - {}) for user {}", startDate, endDate, startTime, endTime, userId);
        List<Menu> mealsDateFiltered = repository.getBetweenHalfOpen(atStartOfDayOrMin(startDate), atStartOfNextDayOrMax(endDate), userId);
        //return MealsUtil.getFilteredTos(mealsDateFiltered, authUser.getUser().getCaloriesPerDay(), startTime, endTime);
        return MenuUtil.getTos(mealsDateFiltered, authUser.getUser().getCaloriesPerDay());
    }*/
}