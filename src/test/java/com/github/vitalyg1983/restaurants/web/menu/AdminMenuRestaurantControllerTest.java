package com.github.vitalyg1983.restaurants.web.menu;

import com.github.vitalyg1983.restaurants.model.Menu;
import com.github.vitalyg1983.restaurants.repository.MenuRepository;
import com.github.vitalyg1983.restaurants.to.MenuTo;
import com.github.vitalyg1983.restaurants.util.JsonUtil;
import com.github.vitalyg1983.restaurants.web.AbstractControllerTest;
import com.github.vitalyg1983.restaurants.util.DateTimeUtil;
import com.github.vitalyg1983.restaurants.web.restaurant.RestaurantTestData;
import com.github.vitalyg1983.restaurants.web.user.UserTestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.github.vitalyg1983.restaurants.web.menu.MenuTestData.rest1Menu1;
import static com.github.vitalyg1983.restaurants.web.menu.MenuTestData.rest2Menu1;
import static com.github.vitalyg1983.restaurants.web.restaurant.RestaurantTestData.REST_ID1;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminMenuRestaurantControllerTest extends AbstractControllerTest {

    private static final String API_ADMIN_URL = "/api/admin/restaurants/" + REST_ID1 + "/menus";

    @Autowired
    private MenuRepository menuRepository;

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(API_ADMIN_URL + "/" + MenuTestData.MENU1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MenuTestData.MENU_MATCHER.contentJson(rest1Menu1));
    }

    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(API_ADMIN_URL + "/" + MenuTestData.MENU1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(API_ADMIN_URL + "/" + MenuTestData.INVALID_MENU_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(API_ADMIN_URL + "/" + MenuTestData.MENU1_ID))
                .andExpect(status().isNoContent());
        assertFalse(menuRepository.get(MenuTestData.MENU1_ID, REST_ID1).isPresent());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void deleteDataConflict() throws Exception {
        perform(MockMvcRequestBuilders.delete(API_ADMIN_URL + "/" + MenuTestData.REST2_MENU1_ID))
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void update() throws Exception {
        MenuTo update = MenuTestData.getNewMenuTo();
        update.setDishIds(Set.of(2, 1));
        perform(MockMvcRequestBuilders.patch(API_ADMIN_URL + "/" + MenuTestData.MENU1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(update)))
                .andExpect(status().isNoContent());

        Menu expectedUpdated = MenuTestData.getUpdated();
        expectedUpdated.setId(MenuTestData.MENU1_ID);
        MenuTestData.MENU_MATCHER.assertMatch(menuRepository.getByDate(rest1Menu1.getMenuDate(), REST_ID1).get(), expectedUpdated);
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void updateInvalid() throws Exception {
        MenuTo invalid = new MenuTo(null, new HashSet<>(List.of()));
        perform(MockMvcRequestBuilders.patch(API_ADMIN_URL + "/" + MenuTestData.MENU1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void createWithLocation() throws Exception {
        MenuTo newMenuTo = MenuTestData.getNewMenuTo();
        ResultActions action = perform(MockMvcRequestBuilders.post(API_ADMIN_URL + "?menuDate=" + MenuTestData.NEW_DATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenuTo)));

        Menu created = MenuTestData.MENU_MATCHER.readFromJson(action);
        int newId = created.id();
        Menu newMenu = MenuTestData.getNew();
        newMenu.setId(newId);
        newMenu.setMenuDate(LocalDate.parse(MenuTestData.NEW_DATE));
        MenuTestData.MENU_MATCHER.assertMatch(created, newMenu);
        MenuTestData.MENU_MATCHER.assertMatch(menuRepository.getById(newId), newMenu);
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void createInvalid() throws Exception {
        MenuTo invalid = new MenuTo(null, new HashSet<>(List.of()));
        perform(MockMvcRequestBuilders.post(API_ADMIN_URL + "?menuDate=" + MenuTestData.NEW_DATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void createDuplicate() throws Exception {
        MenuTo invalid = new MenuTo(null, new HashSet<>(List.of(3, 2, 1)));
        perform(MockMvcRequestBuilders.post(API_ADMIN_URL + "?menuDate=" + rest1Menu1.getMenuDate().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getByDate() throws Exception {
        perform(MockMvcRequestBuilders.get("/api/admin/restaurants/" + RestaurantTestData.REST_ID2 + "/menus/by-date?menuDate=" + rest2Menu1.getMenuDate()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MenuTestData.MENU_MATCHER.contentJson(rest2Menu1));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getAllForRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(API_ADMIN_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MenuTestData.MENU_MATCHER.contentJson(MenuTestData.rest1Menus));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getAllForRestaurantsByDate() throws Exception {
        perform(MockMvcRequestBuilders.get("/api/admin/restaurants/menus/all-by-date?menuDate=" + DateTimeUtil.NOW_DATE))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MenuTestData.MENU_MATCHER.contentJson(MenuTestData.restMenusOnDate));
    }
}