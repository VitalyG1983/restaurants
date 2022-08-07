package com.github.vitaly1983g.restaurants.web.vote;

import com.github.vitaly1983g.restaurants.model.Vote;
import com.github.vitaly1983g.restaurants.util.DateTimeUtil;
import com.github.vitaly1983g.restaurants.web.MatcherFactory;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.github.vitaly1983g.restaurants.util.DateTimeUtil.NOW_DATE_TIME;
import static com.github.vitaly1983g.restaurants.web.user.UserTestData.*;

public class VoteTestData {
    public static MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingEqualsComparator(Vote.class);

    public static final int USER_VOTE1_ID = 1;
    public static final int ADMIN_VOTE1_ID = 3;
    public static final int RESTAURANT_ID1 = 1;
    public static final int RESTAURANT_ID2 = 2;
    public static final int INVALID_RESTAURANT_ID = 99;

    public static final Vote userVote1 = new Vote(USER_VOTE1_ID, LocalDateTime.MIN, user, 1);
    //public static final Vote userVote2 = new Vote(USER_VOTE1_ID + 1, LocalDateTime.of(2020, 1, 6, 10, 0), user, 2);
    public static final Vote adminVote1 = new Vote(ADMIN_VOTE1_ID, LocalDateTime.MAX, admin, 2);
    //public static final Vote adminVote2 = new Vote(ADMIN_VOTE1_ID + 1, LocalDateTime.of(2020, 1, 6, 10, 0), admin, 1);

    public static final List<Vote> todayVotes = List.of(userVote1, adminVote1);
    public static final List<Vote> todayVotesRest1 = List.of(userVote1);

    public static Vote getNew() {
        return new Vote(null, NOW_DATE_TIME, guest, 1);
    }

    public static void setVoteTestTime(LocalTime voteTestTime) {
        DateTimeUtil.VOTE_TEST_TIME = voteTestTime;
    }
}
