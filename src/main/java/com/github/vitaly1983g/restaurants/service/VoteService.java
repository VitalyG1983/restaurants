package com.github.vitaly1983g.restaurants.service;

import com.github.vitaly1983g.restaurants.error.DataConflictException;
import com.github.vitaly1983g.restaurants.model.Vote;
import com.github.vitaly1983g.restaurants.repository.UserRepository;
import com.github.vitaly1983g.restaurants.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.github.vitaly1983g.restaurants.util.DateTimeUtil.TODAY_DATE;
import static com.github.vitaly1983g.restaurants.util.DateTimeUtil.VOTE_ELEVEN_TIME;
import static com.github.vitaly1983g.restaurants.util.Util.isBetweenHalfOpen;

@Service
@AllArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;

    public void update(Vote vote) {
        if (isBetweenHalfOpen(vote.getTimeVote(), LocalTime.MIN, VOTE_ELEVEN_TIME)) {
            voteRepository.save(vote);
        } else throw new DataConflictException("Vote id=" + vote.id() + " can't be updated after 11:00 o'clock");
    }
}
