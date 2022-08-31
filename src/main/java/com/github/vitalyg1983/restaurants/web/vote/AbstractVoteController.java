package com.github.vitalyg1983.restaurants.web.vote;

import com.github.vitalyg1983.restaurants.error.IllegalRequestDataException;
import com.github.vitalyg1983.restaurants.model.Vote;
import com.github.vitalyg1983.restaurants.repository.RestaurantRepository;
import com.github.vitalyg1983.restaurants.repository.VoteRepository;
import com.github.vitalyg1983.restaurants.service.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
public abstract class AbstractVoteController {

    @Autowired
    protected VoteRepository repository;

    @Autowired
    protected RestaurantRepository restaurantRepository;

    @Autowired
    protected VoteService service;

    public ResponseEntity<Vote> getCurrentByToDayDate(int userId) {
        log.info("get current vote on today={} for user id={}", LocalDate.now(), userId);
        return ResponseEntity.of(repository.getCurrentByToDayDate(LocalDate.now(), userId));
    }

    public void update(int userId, int newRestId) {
        log.info("update vote for user {}", userId);
        restaurantRepository.checkExistence(newRestId);
        service.update(repository.checkBelongCurrent(userId), newRestId);
    }

    public ResponseEntity<Vote> createWithLocation(int userId, int restId, String API_URL) {
        log.info("create vote for user {}", userId);
        restaurantRepository.checkExistence(restId);
        Vote created;
        try {
            created = repository.save(new Vote(null, LocalDateTime.now(), userId, restId));
        } catch (DataIntegrityViolationException e) {
            throw new IllegalRequestDataException("User id=" + userId + " can only have one vote for today");
        }
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(API_URL)
                .buildAndExpand().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}