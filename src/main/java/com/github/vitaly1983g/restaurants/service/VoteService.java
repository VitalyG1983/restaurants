package com.github.vitaly1983g.restaurants.service;

import com.github.vitaly1983g.restaurants.error.DataConflictException;
import com.github.vitaly1983g.restaurants.model.Vote;
import com.github.vitaly1983g.restaurants.util.Util;
import com.github.vitaly1983g.restaurants.repository.UserRepository;
import com.github.vitaly1983g.restaurants.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalTime;

import static com.github.vitaly1983g.restaurants.util.DateTimeUtil.TODAY_DATE;
import static com.github.vitaly1983g.restaurants.util.DateTimeUtil.VOTE_ELEVEN_TIME;

@Service
@AllArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;

    public Vote getCurrentVote(int id, int userId) {
        return voteRepository.getCurrentVote(id, TODAY_DATE, userId)
                .orElseThrow(() -> new EntityNotFoundException("Vote id=" + id + " not found on date=" + TODAY_DATE));
    }

    public void update(Vote vote, int userId, int restId) {
        if (vote.getDateVote() == TODAY_DATE && Util.isBetweenHalfOpen(vote.getTimeVote(), LocalTime.MIN, VOTE_ELEVEN_TIME)) {
            save(vote, userId, restId);
        } else throw new DataConflictException("Vote id=" + vote.id() + " can't be updated after 11:00 o'clock");
    }

    @Transactional
    public Vote save(Vote vote, int userId, int restId) {
        vote.setUser(userRepository.getById(userId));
        vote.setRestId(restId);
        return voteRepository.save(vote);
    }
}
