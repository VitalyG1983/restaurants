package com.github.vitalydev.restaurants.web.vote;

import com.github.vitalydev.restaurants.repository.VoteRepository;
import com.github.vitalydev.restaurants.web.AbstractControllerTest;
import com.github.vitalydev.restaurants.web.restaurant.RestaurantTestData;
import com.github.vitalydev.restaurants.web.user.UserTestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

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
        perform(MockMvcRequestBuilders.get(API_URL + "/" + VoteTestData.USER_VOTE1_ID))
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
        perform(MockMvcRequestBuilders.get(API_URL + "/" + VoteTestData.INVALID_VOTE_ID))
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
        VoteTestData.setVoteDeadLineTime(LocalTime.now().plusHours(1));
        perform(MockMvcRequestBuilders.patch(API_URL + "/" + "?newRestId=" + RestaurantTestData.REST_ID1))
                .andExpect(status().isNoContent());

        Assertions.assertEquals(voteRepository.getById(VoteTestData.ADMIN_VOTE1_ID).getRestId(), RestaurantTestData.REST_ID1);
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void updateAfterEleven() throws Exception {
        VoteTestData.setVoteDeadLineTime(LocalTime.now().minusHours(1));
        perform(MockMvcRequestBuilders.patch(API_URL + "/?newRestId=" + RestaurantTestData.REST_ID1))
                .andExpect(status().isConflict());

        Assertions.assertEquals(voteRepository.getById(VoteTestData.ADMIN_VOTE1_ID).getRestId(), RestaurantTestData.REST_ID2);
    }
}