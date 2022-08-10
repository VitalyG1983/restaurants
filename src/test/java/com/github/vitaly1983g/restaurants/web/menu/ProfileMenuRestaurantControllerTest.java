package com.github.vitaly1983g.restaurants.web.menu;

import com.github.vitaly1983g.restaurants.web.AbstractControllerTest;
import com.github.vitaly1983g.restaurants.web.user.UserTestData;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.vitaly1983g.restaurants.web.menu.MenuTestData.*;
import static com.github.vitaly1983g.restaurants.web.restaurant.RestaurantTestData.REST_ID1;
import static com.github.vitaly1983g.restaurants.web.restaurant.RestaurantTestData.REST_ID2;
import static com.github.vitaly1983g.restaurants.web.user.UserTestData.USER_MAIL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfileMenuRestaurantControllerTest extends AbstractControllerTest {

    private static final String API_PROFILE_URL = ProfileMenuRestaurantController.API_URL + '/';

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(API_PROFILE_URL + REST_ID2 + "/menus/by-date?menuDate=2020-01-31"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(rest2Menu1));
    }

    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(API_PROFILE_URL + REST_ID1 + "/menus/" + MENU1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAllForRestaurantsByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(API_PROFILE_URL + "menus/all-by-date?menuDate=2020-01-31"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(restMenusOnDate));
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get("/api/admin/restaurants/"))
                .andExpect(status().isForbidden());
    }
}