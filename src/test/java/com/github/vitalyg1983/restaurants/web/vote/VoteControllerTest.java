package com.github.vitalyg1983.restaurants.web.vote;

import com.github.vitalyg1983.restaurants.model.Vote;
import com.github.vitalyg1983.restaurants.repository.VoteRepository;
import com.github.vitalyg1983.restaurants.web.AbstractControllerTest;
import com.github.vitalyg1983.restaurants.web.user.UserTestData;
import com.github.vitalyg1983.restaurants.util.DateTimeUtil;
import com.github.vitalyg1983.restaurants.web.restaurant.RestaurantTestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
        perform(MockMvcRequestBuilders.get(API_URL + "/" + VoteTestData.USER_VOTE1_ID + "?restId=1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VoteTestData.VOTE_MATCHER.contentJson(VoteTestData.userVote1));
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getCurrent() throws Exception {
        perform(MockMvcRequestBuilders.get(API_URL + "/get-current?id=" + VoteTestData.USER_VOTE1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VoteTestData.VOTE_MATCHER.contentJson(VoteTestData.userVote1));
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(API_URL + "/get-by-date?voteDate=" + VoteTestData.userVote2.getDateVote()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VoteTestData.VOTE_MATCHER.contentJson(VoteTestData.userVote2));
    }

    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(API_URL + "/" + VoteTestData.USER_VOTE1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get("/api/admin/votes/"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(API_URL + "/" + VoteTestData.ADMIN_VOTE1_ID + "?restId=" + RestaurantTestData.REST_ID2))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get("/api/admin/votes?voteDate=" + DateTimeUtil.NOW_DATE))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VoteTestData.VOTE_MATCHER.contentJson(VoteTestData.todayVotes));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getAllForRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get("/api/admin/votes/for-restaurant")
                .param("restId", String.valueOf(RestaurantTestData.REST_ID1)).param("voteDate", String.valueOf(DateTimeUtil.NOW_DATE)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VoteTestData.VOTE_MATCHER.contentJson(VoteTestData.todayVotesRest1));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete("/api/admin/votes/"
                + VoteTestData.USER_VOTE1_ID + "?restId=" + RestaurantTestData.REST_ID1 + "&userId=" + UserTestData.USER_ID))
                .andExpect(status().isNoContent());

        assertFalse(voteRepository.findById(VoteTestData.USER_VOTE1_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void deleteDataConflict() throws Exception {
        perform(MockMvcRequestBuilders.delete("/api/admin/votes/"
                + VoteTestData.INVALID_VOTE_ID + "?restId=" + RestaurantTestData.REST_ID1 + "&userId=" + UserTestData.USER_ID))
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = UserTestData.GUEST_MAIL)
    void createWithLocation() throws Exception {
        Vote newVote = VoteTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(API_URL + "/" + "?restId=1"));

        Vote created = VoteTestData.VOTE_MATCHER.readFromJson(action);
        int newId = created.id();
        newVote.setId(newId);
        VoteTestData.VOTE_MATCHER.assertMatch(created, newVote);
        VoteTestData.VOTE_MATCHER.assertMatch(voteRepository.getById(newId), newVote);
    }

    @Test
    @WithUserDetails(value = UserTestData.GUEST_MAIL)
    void createInvalid() throws Exception {
        perform(MockMvcRequestBuilders.post(API_URL + "/" + "?restId=" + RestaurantTestData.INVALID_RESTAURANT_ID))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void createDuplicate() throws Exception {
        perform(MockMvcRequestBuilders.post(API_URL + "/?restId=" + RestaurantTestData.REST_ID1))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTestData.GUEST_MAIL)
    void updateInvalid() throws Exception {
        perform(MockMvcRequestBuilders.patch(API_URL + "/" + VoteTestData.USER_VOTE1_ID + "?newRestId=" + RestaurantTestData.INVALID_RESTAURANT_ID))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void updateBeforeEleven() throws Exception {
        VoteTestData.setVoteTestTime(DateTimeUtil.VOTE_ELEVEN_TIME.minusHours(1));
        perform(MockMvcRequestBuilders.patch(API_URL + "/" + VoteTestData.USER_VOTE1_ID + "?newRestId=" + RestaurantTestData.REST_ID2))
                .andExpect(status().isNoContent());

        assertEquals(voteRepository.getById(VoteTestData.USER_VOTE1_ID).getRestId(), RestaurantTestData.REST_ID2);
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void updateAfterEleven() throws Exception {
        VoteTestData.setVoteTestTime(DateTimeUtil.VOTE_ELEVEN_TIME.plusHours(1));
        perform(MockMvcRequestBuilders.patch(API_URL + "/" + VoteTestData.USER_VOTE1_ID + "?newRestId=" + RestaurantTestData.REST_ID2))
                .andExpect(status().isConflict());

        assertEquals(voteRepository.getById(VoteTestData.USER_VOTE1_ID).getRestId(), RestaurantTestData.REST_ID1);
    }
}