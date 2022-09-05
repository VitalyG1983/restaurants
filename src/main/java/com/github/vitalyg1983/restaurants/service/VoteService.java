package com.github.vitalyg1983.restaurants.service;

import com.github.vitalyg1983.restaurants.error.DataConflictException;
import com.github.vitalyg1983.restaurants.error.IllegalRequestDataException;
import com.github.vitalyg1983.restaurants.model.Vote;
import com.github.vitalyg1983.restaurants.repository.RestaurantRepository;
import com.github.vitalyg1983.restaurants.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.github.vitalyg1983.restaurants.util.DateTimeUtil.VOTE_ELEVEN_TIME;

@Service
@AllArgsConstructor
public class VoteService {
    private VoteRepository repository;

    public void update(int userId, int newRestId) {
        Vote vote = repository.checkBelongCurrent(userId);
        vote.setTimeVote(LocalTime.now());
        if (vote.getTimeVote().isBefore(VOTE_ELEVEN_TIME)) {
            vote.setRestId(newRestId);
        } else {
            throw new DataConflictException("Vote id=" + vote.id() + " can't be updated after 11:00 o'clock");
        }
    }

    public Vote create(int userId, int restId) {
        try {
            return repository.save(new Vote(null, LocalDateTime.now(), userId, restId));
        } catch (DataIntegrityViolationException e) {
            throw new IllegalRequestDataException("User id=" + userId + " can only have one vote for today");
        }
    }
}
