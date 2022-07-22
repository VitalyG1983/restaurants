package com.github.vitaly1983g.restaurants.web.dish;

import com.github.vitaly1983g.restaurants.model.Menu;
import com.github.vitaly1983g.restaurants.repository.MenuRepository;
import com.github.vitaly1983g.restaurants.service.MenuService;
import com.github.vitaly1983g.restaurants.to.LunchTo;
import com.github.vitaly1983g.restaurants.util.MealsUtil;
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
import java.time.LocalTime;
import java.util.List;

import static com.github.vitaly1983g.restaurants.util.DateTimeUtil.atStartOfDayOrMin;
import static com.github.vitaly1983g.restaurants.util.DateTimeUtil.atStartOfNextDayOrMax;
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

    @GetMapping("/{menuId}")
    public ResponseEntity<Menu> get(@PathVariable int menuId, @PathVariable int restId) {
        log.info("get menu with id={} of restaurant {}", menuId, restId);
        return ResponseEntity.of(repository.get(menuId, restId));
    }

    @GetMapping("/by-date")
    public ResponseEntity<Menu> getByDate(@PathVariable int restId,
                                    @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
        log.info("get menu on date {} of restaurant {}", menuDate, restId);
        return ResponseEntity.of(repository.getByDate(menuDate, restId));
    }

    @GetMapping("/{menuId}")
    public ResponseEntity<Menu> getWithDish(@PathVariable int menuId, @PathVariable int restId) {
        log.info("get menu {} of restaurant {}", menuId, restId);
        return ResponseEntity.of(repository.getWithDish(menuId, restId));
    }

    @GetMapping
    public List<Menu> getAll(@PathVariable int restId) {
        log.info("getAll of restaurant {}", restId);
        return repository.getAll(restId);
    }

    @DeleteMapping("/{menuId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int menuId, @PathVariable int restId) {
        log.info("delete menu {} of restaurant {}", menuId, restId);
        Menu menu = repository.checkBelong(menuId, restId);
        repository.delete(menu);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody Menu menu, @PathVariable int id) {
        int userId = authUser.id();
        log.info("update {} for user {}", menu, userId);
        assureIdConsistent(menu, id);
        repository.checkBelong(id, userId);
        service.save(menu, userId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Menu> createWithLocation(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody Menu menu) {
        int userId = authUser.id();
        log.info("create {} for user {}", menu, userId);
        checkNew(menu);
        Menu created = service.save(menu, userId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }


    @GetMapping("/filter")
    public List<LunchTo> getBetween(@AuthenticationPrincipal AuthUser authUser,
                                    @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                    @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
                                    @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                    @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {

        int userId = authUser.id();
        log.info("getBetween dates({} - {}) time({} - {}) for user {}", startDate, endDate, startTime, endTime, userId);
        List<Menu> mealsDateFiltered = repository.getBetweenHalfOpen(atStartOfDayOrMin(startDate), atStartOfNextDayOrMax(endDate), userId);
        //return MealsUtil.getFilteredTos(mealsDateFiltered, authUser.getUser().getCaloriesPerDay(), startTime, endTime);
        return MealsUtil.getTos(mealsDateFiltered, authUser.getUser().getCaloriesPerDay());
    }
}