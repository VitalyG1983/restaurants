package com.github.vitaly1983g.restaurants.web.dish;

import com.github.vitaly1983g.restaurants.to.LunchTo;
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
import com.github.vitaly1983g.restaurants.model.Menu;
import com.github.vitaly1983g.restaurants.repository.MenuRepository;
import com.github.vitaly1983g.restaurants.service.MenuService;
import com.github.vitaly1983g.restaurants.util.MealsUtil;
import com.github.vitaly1983g.restaurants.web.AuthUser;

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
@RequestMapping(value = MealController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class MealController {
    static final String REST_URL = "/api/profile/meals";

    private final MenuRepository repository;
    private final MenuService service;

    @GetMapping("/{id}")
    public ResponseEntity<Menu> get(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        log.info("get dish {} for user {}", id, authUser.id());
        return ResponseEntity.of(repository.get(id, authUser.id()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        log.info("delete {} for user {}", id, authUser.id());
        Menu menu = repository.checkBelong(id, authUser.id());
        repository.delete(menu);
    }

    @GetMapping
    public List<LunchTo> getAll(@AuthenticationPrincipal AuthUser authUser) {
        log.info("getAll for user {}", authUser.id());
        return MealsUtil.getTos(repository.getAll(authUser.id()), authUser.getUser().getCaloriesPerDay());
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