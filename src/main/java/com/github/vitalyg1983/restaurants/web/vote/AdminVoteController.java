package com.github.vitalyg1983.restaurants.web.vote;

import com.github.vitalyg1983.restaurants.model.Vote;
import com.github.vitalyg1983.restaurants.web.AuthUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = AdminVoteController.API_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class AdminVoteController extends AbstractVoteController {
    static final String API_URL = "/api/admin/votes";

    @GetMapping("/{id}")
    public ResponseEntity<Vote> get(@PathVariable int id) {
        log.info("get vote id={}", id);
        return ResponseEntity.of(repository.findById(id));
    }

    @GetMapping("/by-today")
    public ResponseEntity<Vote> getCurrentByToDayDate(@AuthenticationPrincipal AuthUser authUser) {
        return super.getCurrentByToDayDate(authUser.id());
    }

    @GetMapping("/for-restaurant")
    public List<Vote> getAllForRestaurant(@RequestParam int restId,
                                          @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate voteDate) {
        log.info("getAll votes for restaurant id={} on date={}", restId, voteDate == null ? LocalDate.now() : voteDate);
        return repository.getAllForRestaurant(restId, voteDate == null ? LocalDate.now() : voteDate);
    }

    @GetMapping()
    public List<Vote> getAll(@RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate voteDate) {
        log.info("getAll votes for restaurants on date={}", voteDate == null ? LocalDate.now() : voteDate);
        return repository.getAll(voteDate == null ? LocalDate.now() : voteDate);
    }

    @Transactional
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id, @RequestParam int restId, @RequestParam int userId) {
        log.info("delete vote id={} for user {}", id, userId);
        Vote vote = repository.checkBelong(id, userId, restId);
        repository.delete(vote);
    }

    @PatchMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@AuthenticationPrincipal AuthUser authUser, @RequestParam int newRestId) {
        super.update(authUser.id(), newRestId);
    }

    @PostMapping()
    public ResponseEntity<Vote> createWithLocation(@AuthenticationPrincipal AuthUser authUser, @RequestParam int restId) {
        return super.createWithLocation(authUser.id(), restId, API_URL + "/by-today");
    }
}