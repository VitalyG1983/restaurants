package com.github.vitalyg1983.restaurants.service;

import com.github.vitalyg1983.restaurants.error.DataConflictException;
import com.github.vitalyg1983.restaurants.model.Vote;
import com.github.vitalyg1983.restaurants.util.DateTimeUtil;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Arrays;

import static com.github.vitalyg1983.restaurants.util.Util.TEST_PROFILE;
import static com.github.vitalyg1983.restaurants.util.Util.isBetweenHalfOpen;

@Service
@AllArgsConstructor
public class VoteService {
    private final Environment environment;

    public void update(Vote vote, int newRestId) {
        String[] activeProfiles = environment.getActiveProfiles();
        if (Arrays.toString(activeProfiles).contains(TEST_PROFILE)) {
            vote.setTimeVote(DateTimeUtil.VOTE_TEST_TIME);
        } else {
            vote.setTimeVote(LocalTime.now());
        }
        if (isBetweenHalfOpen(vote.getTimeVote(), LocalTime.MIN, DateTimeUtil.VOTE_ELEVEN_TIME)) {
            vote.setRestId(newRestId);
        } else throw new DataConflictException("Vote id=" + vote.id() + " can't be updated after 11:00 o'clock");
    }
}
