package com.github.vitaly1983g.restaurants.web.menu;

import com.github.vitaly1983g.restaurants.model.Menu;
import com.github.vitaly1983g.restaurants.repository.MenuRepository;
import com.github.vitaly1983g.restaurants.repository.RestaurantRepository;
import com.github.vitaly1983g.restaurants.to.MenuTo;
import com.github.vitaly1983g.restaurants.util.JsonUtil;
import com.github.vitaly1983g.restaurants.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.github.vitaly1983g.restaurants.util.DateTimeUtil.NOW_DATE;
import static com.github.vitaly1983g.restaurants.web.menu.MenuTestData.*;
import static com.github.vitaly1983g.restaurants.web.restaurant.RestaurantTestData.REST_ID1;
import static com.github.vitaly1983g.restaurants.web.restaurant.RestaurantTestData.rest2;
import static com.github.vitaly1983g.restaurants.web.user.UserTestData.ADMIN_MAIL;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminMenuRestaurantControllerTest extends AbstractControllerTest {

    private static final String API_ADMIN_URL = AdminMenuRestaurantController.API_URL + '/';
    // private static final String API_PROFILE_URL = ProfileMenuRestaurantController.API_URL + '/';

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(API_ADMIN_URL + REST_ID1 + "/menus/" + MENU1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(rest1Menu1));
    }

    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(API_ADMIN_URL + REST_ID1 + "/menus/" + MENU1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(API_ADMIN_URL + REST_ID1 + "/menus/" + MENU1_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(API_ADMIN_URL + REST_ID1 + "/menus/" + MENU1_ID))
                .andExpect(status().isNoContent());
        assertFalse(menuRepository.get(MENU1_ID, REST_ID1).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteDataConflict() throws Exception {
        perform(MockMvcRequestBuilders.delete(API_ADMIN_URL + REST_ID1 + "/menus/" + REST2_MENU1_ID))
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        MenuTo update = MenuTestData.getNewMenuTo();
        update.setDishIds(Set.of(2, 1));
        ResultActions action =perform(MockMvcRequestBuilders.put(API_ADMIN_URL + REST_ID1 + "/menus/" + MENU1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(update)))
                .andExpect(status().isNoContent());

        Menu expectedUpdated = MenuTestData.getUpdated();
        expectedUpdated.setId(MENU1_ID);
        //MENU_MATCHER.assertMatch(actualUpdated, expectedUpdated);
        MENU_MATCHER.assertMatch(menuRepository.getById(MENU1_ID), expectedUpdated);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateInvalid() throws Exception {
        Menu invalid = new Menu(null, NOW_DATE, null);
        perform(MockMvcRequestBuilders.put(API_ADMIN_URL + REST_ID1 + "/menus/" + MENU1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = ADMIN_MAIL)
    void updateDuplicate() {
        Menu invalid = new Menu(MENU1_ID, rest2Menu1.getMenuDate(), rest2);
        assertThrows(Exception.class, () ->
                perform(MockMvcRequestBuilders.put(API_ADMIN_URL + MENU1_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(invalid)))
                        .andDo(print())
        );
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        MenuTo newMenuTo = MenuTestData.getNewMenuTo();
        ResultActions action = perform(MockMvcRequestBuilders.post(API_ADMIN_URL + REST_ID1 + "/menus?menuDate=" + NEW_DATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenuTo)));

        Menu created = MENU_MATCHER.readFromJson(action);
        int newId = created.id();
        Menu newMenu = MenuTestData.getNew();
        newMenu.setId(newId);
        MENU_MATCHER.assertMatch(created, newMenu);
        MENU_MATCHER.assertMatch(menuRepository.getById(newId), newMenu);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createInvalid() throws Exception {
        MenuTo invalid = new MenuTo(null, new HashSet<>(List.of()));
        perform(MockMvcRequestBuilders.post(API_ADMIN_URL + REST_ID1 + "/menus?menuDate=" + NEW_DATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = ADMIN_MAIL)
    void createDuplicate() throws Exception {
        MenuTo invalid = new MenuTo(null, new HashSet<>(List.of(3, 2, 1)));
        perform(MockMvcRequestBuilders.post(API_ADMIN_URL + REST_ID1 + "/menus?menuDate=" + rest1Menu1.getMenuDate().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(API_ADMIN_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_WITH_DISHES_MATCHER.contentJson(rest1Menu1));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getBetween() throws Exception {
        perform(MockMvcRequestBuilders.get(API_ADMIN_URL + "filter")
                .param("startDate", "2020-01-30").param("startTime", "07:00")
                .param("endDate", "2020-01-31").param("endTime", "11:00"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MENU_WITH_DISHES_MATCHER.contentJson(rest1Menu1));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getBetweenAll() throws Exception {
        perform(MockMvcRequestBuilders.get(API_ADMIN_URL + "filter?startDate=&endTime="))
                .andExpect(status().isOk())
                .andExpect(MENU_WITH_DISHES_MATCHER.contentJson(rest1Menu1));
    }


}