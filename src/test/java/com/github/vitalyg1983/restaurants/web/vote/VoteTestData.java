package com.github.vitalyg1983.restaurants.web.vote;

import com.github.vitalyg1983.restaurants.model.Vote;
import com.github.vitalyg1983.restaurants.util.DateTimeUtil;
import com.github.vitalyg1983.restaurants.web.MatcherFactory;
import com.github.vitalyg1983.restaurants.web.user.UserTestData;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class VoteTestData {
    public static MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingEqualsComparator(Vote.class);

    public static final int USER_VOTE1_ID = 1;
    public static final int ADMIN_VOTE1_ID = 3;
    public static final int INVALID_VOTE_ID = 99;

    public static final Vote userVote1 = new Vote(USER_VOTE1_ID, LocalDateTime.MIN, UserTestData.user, 1);
    public static final Vote userVote2 = new Vote(USER_VOTE1_ID + 1, LocalDateTime.of(2020, 1, 6, 0, 0, 0), UserTestData.user, 3);
    public static final Vote adminVote1 = new Vote(ADMIN_VOTE1_ID, LocalDateTime.MAX, UserTestData.admin, 2);

    public static final List<Vote> todayVotes = List.of(userVote1, adminVote1);
    public static final List<Vote> todayVotesRest1 = List.of(userVote1);

    public static Vote getNew() {
        return new Vote(null, DateTimeUtil.NOW_DATE_TIME, UserTestData.guest, 1);
    }

    public static void setVoteTestTime(LocalTime voteTestTime) {
        DateTimeUtil.VOTE_TEST_TIME = voteTestTime;
    }
}
