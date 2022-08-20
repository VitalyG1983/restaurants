package com.github.vitalyg1983.restaurants.web.menu;

import com.github.vitalyg1983.restaurants.web.AbstractControllerTest;
import com.github.vitalyg1983.restaurants.web.user.UserTestData;
import com.github.vitalyg1983.restaurants.util.DateTimeUtil;
import com.github.vitalyg1983.restaurants.web.restaurant.RestaurantTestData;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfileMenuRestaurantControllerTest extends AbstractControllerTest {

    private static final String API_PROFILE_URL = ProfileMenuRestaurantController.API_URL + '/';

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(API_PROFILE_URL + RestaurantTestData.REST_ID2 + "/menus/by-date?menuDate=" + MenuTestData.rest2Menu1.getMenuDate()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MenuTestData.MENU_MATCHER.contentJson(MenuTestData.rest2Menu1));
    }

    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(API_PROFILE_URL + RestaurantTestData.REST_ID1 + "/menus/" + MenuTestData.MENU1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getAllForRestaurantsByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(API_PROFILE_URL + "menus/all-by-date?menuDate=" + DateTimeUtil.NOW_DATE))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MenuTestData.MENU_MATCHER.contentJson(MenuTestData.restMenusOnDate));
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get("/api/admin/restaurants/"))
                .andExpect(status().isForbidden());
    }
}