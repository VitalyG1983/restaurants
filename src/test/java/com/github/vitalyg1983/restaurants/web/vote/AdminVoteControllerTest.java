package com.github.vitalyg1983.restaurants.web.vote;

import com.github.vitalyg1983.restaurants.repository.VoteRepository;
import com.github.vitalyg1983.restaurants.util.DateTimeUtil;
import com.github.vitalyg1983.restaurants.web.AbstractControllerTest;
import com.github.vitalyg1983.restaurants.web.restaurant.RestaurantTestData;
import com.github.vitalyg1983.restaurants.web.user.UserTestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.github.vitalyg1983.restaurants.web.vote.VoteTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminVoteControllerTest extends AbstractControllerTest {

    private static final String API_URL = "/api/admin/votes";

    @Autowired
    private VoteRepository voteRepository;

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(API_URL + "/" + USER_VOTE1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VoteTestData.VOTE_MATCHER.contentJson(VoteTestData.userVote1));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getCurrentByToDayDate() throws Exception {
        perform(MockMvcRequestBuilders.get(API_URL + "/by-today"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VoteTestData.VOTE_MATCHER.contentJson(VoteTestData.adminVote1));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(API_URL + "/" + INVALID_VOTE_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(API_URL + "/?voteDate=" + LocalDate.now()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VoteTestData.VOTE_MATCHER.contentJson(VoteTestData.todayVotes));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getAllForRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(API_URL + "/for-restaurant")
                .param("restId", String.valueOf(RestaurantTestData.REST_ID1)).param("voteDate", String.valueOf(LocalDate.now())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VoteTestData.VOTE_MATCHER.contentJson(VoteTestData.todayVotesRest1));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete("/api/admin/votes/"
                + USER_VOTE1_ID + "?restId=" + RestaurantTestData.REST_ID1 + "&userId=" + UserTestData.USER_ID))
                .andExpect(status().isNoContent());

        assertFalse(voteRepository.findById(USER_VOTE1_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void deleteDataConflict() throws Exception {
        perform(MockMvcRequestBuilders.delete("/api/admin/votes/"
                + INVALID_VOTE_ID + "?restId=" + RestaurantTestData.REST_ID1 + "&userId=" + UserTestData.USER_ID))
                .andExpect(status().isConflict());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void createDuplicate() throws Exception {
        perform(MockMvcRequestBuilders.post(API_URL + "/?restId=" + RestaurantTestData.REST_ID1))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void updateInvalid() throws Exception {
        perform(MockMvcRequestBuilders.patch(API_URL + "/?newRestId=" + RestaurantTestData.INVALID_RESTAURANT_ID))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void updateBeforeEleven() throws Exception {
        VoteTestData.setVoteTestTime(DateTimeUtil.VOTE_ELEVEN_TIME.minusHours(1));
        perform(MockMvcRequestBuilders.patch(API_URL + "/" + "?newRestId=" + RestaurantTestData.REST_ID1))
                .andExpect(status().isNoContent());

        assertEquals(voteRepository.getById(ADMIN_VOTE1_ID).getRestId(), RestaurantTestData.REST_ID1);
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void updateAfterEleven() throws Exception {
        VoteTestData.setVoteTestTime(DateTimeUtil.VOTE_ELEVEN_TIME.plusHours(1));
        perform(MockMvcRequestBuilders.patch(API_URL + "/?newRestId=" + RestaurantTestData.REST_ID1))
                .andExpect(status().isConflict());

        assertEquals(voteRepository.getById(ADMIN_VOTE1_ID).getRestId(), RestaurantTestData.REST_ID2);
    }
}