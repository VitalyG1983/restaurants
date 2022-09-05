package com.github.vitalydev.restaurants.service;

import com.github.vitalydev.restaurants.error.DataConflictException;
import com.github.vitalydev.restaurants.error.IllegalRequestDataException;
import com.github.vitalydev.restaurants.util.DateTimeUtil;
import com.github.vitalydev.restaurants.model.Vote;
import com.github.vitalydev.restaurants.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@AllArgsConstructor
public class VoteService {
    private VoteRepository repository;

    public void update(int userId, int newRestId) {
        Vote vote = repository.checkBelongCurrent(userId);
        vote.setTimeVote(LocalTime.now());
        if (vote.getTimeVote().isBefore(DateTimeUtil.VOTE_ELEVEN_TIME)) {
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
