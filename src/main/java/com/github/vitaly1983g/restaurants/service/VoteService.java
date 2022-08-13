package com.github.vitaly1983g.restaurants.service;

import com.github.vitaly1983g.restaurants.error.DataConflictException;
import com.github.vitaly1983g.restaurants.model.Vote;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Arrays;

import static com.github.vitaly1983g.restaurants.util.DateTimeUtil.VOTE_ELEVEN_TIME;
import static com.github.vitaly1983g.restaurants.util.DateTimeUtil.VOTE_TEST_TIME;
import static com.github.vitaly1983g.restaurants.util.Util.TEST_PROFILE;
import static com.github.vitaly1983g.restaurants.util.Util.isBetweenHalfOpen;

@Service
@AllArgsConstructor
public class VoteService {
    private final Environment environment;

    public void update(Vote vote, int newRestId) {
        String[] activeProfiles = environment.getActiveProfiles();
        if (Arrays.toString(activeProfiles).contains(TEST_PROFILE)) {
            vote.setTimeVote(VOTE_TEST_TIME);
        } else {
            vote.setTimeVote(LocalTime.now());
        }
        if (isBetweenHalfOpen(vote.getTimeVote(), LocalTime.MIN, VOTE_ELEVEN_TIME)) {
            vote.setRestId(newRestId);
        } else throw new DataConflictException("Vote id=" + vote.id() + " can't be updated after 11:00 o'clock");
    }
}
