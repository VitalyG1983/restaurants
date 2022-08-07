package com.github.vitaly1983g.restaurants.web.vote;

import com.github.vitaly1983g.restaurants.model.Vote;
import com.github.vitaly1983g.restaurants.repository.VoteRepository;
import com.github.vitaly1983g.restaurants.web.AbstractControllerTest;
import com.github.vitaly1983g.restaurants.web.user.UserTestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.github.vitaly1983g.restaurants.util.DateTimeUtil.NOW_DATE;
import static com.github.vitaly1983g.restaurants.util.DateTimeUtil.VOTE_ELEVEN_TIME;
import static com.github.vitaly1983g.restaurants.web.vote.VoteTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VoteControllerTest extends AbstractControllerTest {

    private static final String API_URL = "/api/votes";

    @Autowired
    private VoteRepository voteRepository;

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(API_URL + "/" + USER_VOTE1_ID + "?restId=1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(userVote1));
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getCurrentVote() throws Exception {
        perform(MockMvcRequestBuilders.get(API_URL + "/get-current?id=" + USER_VOTE1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(userVote1));
    }

    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(API_URL + "/" + USER_VOTE1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(API_URL + "/" + ADMIN_VOTE1_ID + "?restId=" + RESTAURANT_ID2))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get("/api/admin/votes?voteDate=" + NOW_DATE))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(todayVotes));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getAllForRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get("/api/admin/votes/for-restaurant")
                .param("restId", String.valueOf(RESTAURANT_ID1)).param("voteDate", String.valueOf(NOW_DATE)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(todayVotesRest1));
    }

    @Test
    @WithUserDetails(value = UserTestData.GUEST_MAIL)
    void createWithLocation() throws Exception {
        Vote newVote = VoteTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(API_URL + "/" + "?restId=1"));

        Vote created = VOTE_MATCHER.readFromJson(action);
        int newId = created.id();
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(voteRepository.getById(newId), newVote);
    }

    @Test
    @WithUserDetails(value = UserTestData.GUEST_MAIL)
    void createInvalid() throws Exception {
        perform(MockMvcRequestBuilders.post(API_URL + "/" + "?restId=" + INVALID_RESTAURANT_ID))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void createDuplicate() throws Exception {
        perform(MockMvcRequestBuilders.post(API_URL + "/?restId=" + RESTAURANT_ID1))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTestData.GUEST_MAIL)
    void updateInvalid() throws Exception {
        perform(MockMvcRequestBuilders.put(API_URL + "/" + USER_VOTE1_ID + "?newRestId=" + INVALID_RESTAURANT_ID))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void updateBeforeEleven() throws Exception {
        setVoteTestTime(VOTE_ELEVEN_TIME.minusHours(1));
        perform(MockMvcRequestBuilders.put(API_URL + "/" + USER_VOTE1_ID + "?newRestId=" + RESTAURANT_ID2))
                .andExpect(status().isNoContent());

        assertEquals(voteRepository.getById(USER_VOTE1_ID).getRestId(), RESTAURANT_ID2);
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void updateAfterEleven() throws Exception {
        setVoteTestTime(VOTE_ELEVEN_TIME.plusHours(1));
        perform(MockMvcRequestBuilders.put(API_URL + "/" + USER_VOTE1_ID + "?newRestId=" + RESTAURANT_ID2))
                .andExpect(status().isUnprocessableEntity());

        assertEquals(voteRepository.getById(USER_VOTE1_ID).getRestId(), RESTAURANT_ID1);
    }
}