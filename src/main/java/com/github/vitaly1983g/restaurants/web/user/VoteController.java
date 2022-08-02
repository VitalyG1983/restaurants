package com.github.vitaly1983g.restaurants.web.user;

import com.github.vitaly1983g.restaurants.model.Vote;
import com.github.vitaly1983g.restaurants.repository.VoteRepository;
import com.github.vitaly1983g.restaurants.service.VoteService;
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

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.github.vitaly1983g.restaurants.util.DateTimeUtil.TODAY_DATE;


@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class VoteController {
    static final String REST_URL = "/api";
    private final VoteRepository repository;
    private final VoteService service;

    @GetMapping("/votes/{id}")
    public ResponseEntity<Vote> getVote(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id, @RequestParam int restId) {
        log.info("get vote id={} for user id={}", id, authUser.id());
        return ResponseEntity.of(repository.get(id, authUser.id(), restId));
    }

    @GetMapping("/votes/get-current")
    public ResponseEntity<Vote> getCurrentVote(@AuthenticationPrincipal AuthUser authUser, @RequestParam int id) {
        log.info("get current vote id={} for user id={}", id, authUser.id());
        return ResponseEntity.of(repository.getCurrentVote(id, TODAY_DATE, authUser.id()));
    }

    @GetMapping("/admin/votes/for-restaurant")
    public List<Vote> getAllForRestaurant(@RequestParam int restId,
                                          @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate voteDate) {
        log.info("getAll votes for restaurants id={} on date={}", restId, voteDate);
        return repository.getAllForRestaurant(restId, voteDate);
    }

    @GetMapping("/admin/votes")
    public List<Vote> getAll(@RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate voteDate) {
        log.info("getAll votes for restaurants on date={}", voteDate);
        return repository.getAll(voteDate);
    }

    // if user can delete his vote
/*    @DeleteMapping("/votes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id, @RequestParam int restId) {
        log.info("delete vote id={} for user {}", id, authUser.id());
        Vote vote = repository.checkBelong(id, authUser.id(), restId);
        repository.delete(vote);
    }*/

    @PutMapping(value = "/votes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@AuthenticationPrincipal AuthUser authUser, @RequestParam int restId, @PathVariable int id) {
        int userId = authUser.id();
        log.info("update vote for user {}", userId);
        service.update(repository.checkBelongCurrentVote(id, userId, restId));
    }

    @PostMapping(value = "/votes")
    public ResponseEntity<Vote> createWithLocation(@AuthenticationPrincipal AuthUser authUser, @RequestParam int restId) {
        int userId = authUser.id();
        log.info("create vote for user {}", userId);
        Vote created = repository.save(new Vote(null, LocalDateTime.now(), authUser.getUser(), restId));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}