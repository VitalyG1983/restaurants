package com.github.vitaly1983g.restaurants.web.user;

import com.github.vitaly1983g.restaurants.model.Vote;
import com.github.vitaly1983g.restaurants.service.VoteService;
import com.github.vitaly1983g.restaurants.util.DateTimeUtil;
import com.github.vitaly1983g.restaurants.util.validation.ValidationUtil;
import com.github.vitaly1983g.restaurants.web.AuthUser;
import com.github.vitaly1983g.restaurants.repository.VoteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;


@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class VoteController {
    static final String REST_URL = "/api";
    private final VoteRepository repository;
    private final VoteService service;

    @GetMapping("/profile/votes/{id}")
    public ResponseEntity<Vote> getVote(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id, @RequestParam int restId) {
        log.info("get vote id={} for user id={}", id, authUser.id());
        return ResponseEntity.of(repository.get(id, authUser.id(), restId));
    }

    @GetMapping("/profile/votes/get-current")
    public Vote getCurrentVote(@AuthenticationPrincipal AuthUser authUser, @RequestParam int id) {
        log.info("get current vote id={} for user id={}", id, authUser.id());
        return service.getCurrentVote(id, authUser.id());
    }

    @GetMapping("/admin/votes/for-restaurant")
    public List<Vote> getAllForRestaurant(@RequestParam int restId) {
        log.info("getAll votes for restaurants id={} on date={}", restId, DateTimeUtil.TODAY_DATE);
        return repository.getAllForRestaurant(restId, DateTimeUtil.TODAY_DATE);
    }

    @GetMapping("/admin/votes")
    public List<Vote> getAll() {
        log.info("getAll votes for restaurants on date={}", DateTimeUtil.TODAY_DATE);
        return repository.getAll(DateTimeUtil.TODAY_DATE);
    }

    @DeleteMapping("/profile/votes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id, @RequestParam int restId) {
        log.info("delete vote id={} for user {}", id, authUser.id());
        Vote vote = repository.checkBelong(id, authUser.id(), restId);
        repository.delete(vote);
    }

    @PutMapping(value = "/profile/votes/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody Vote vote, @PathVariable int id,
                       @RequestParam int restId) {
        int userId = authUser.id();
        log.info("update vote {} for user {}", vote, userId);
        ValidationUtil.assureIdConsistent(vote, id);
        repository.checkBelongCurrentVote(id, userId, restId);
        service.update(vote, userId, restId);
    }

    @PostMapping(value = "/profile/votes", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> createWithLocation(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody Vote vote,
                                                   @RequestParam int restId) {
        int userId = authUser.id();
        log.info("create vote {} for user {}", vote, userId);
        ValidationUtil.checkNew(vote);
        Vote created = service.save(vote, userId, restId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }


  /*  @GetMapping("/filter")
    public List<MealTo> getBetween(@AuthenticationPrincipal AuthUser authUser,
                                   @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                   @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
                                   @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                   @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {

        int userId = authUser.id();
        log.info("getBetween dates({} - {}) time({} - {}) for user {}", startDate, endDate, startTime, endTime, userId);
        List<Vote> mealsDateFiltered = repository.getBetweenHalfOpen(atStartOfDayOrMin(startDate), atStartOfNextDayOrMax(endDate), userId);
        return MealsUtil.getFilteredTos(mealsDateFiltered, authUser.getUser().getCaloriesPerDay(), startTime, endTime);
    }*/
}