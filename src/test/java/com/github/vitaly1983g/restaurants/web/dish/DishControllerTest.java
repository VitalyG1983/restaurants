package com.github.vitaly1983g.restaurants.web.dish;


import com.github.vitaly1983g.restaurants.model.Dish;
import com.github.vitaly1983g.restaurants.repository.DishRepository;
import com.github.vitaly1983g.restaurants.util.JsonUtil;
import com.github.vitaly1983g.restaurants.util.MealsUtil;
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

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DishControllerTest extends AbstractControllerTest {

    private static final String REST_URL = MealController.REST_URL + '/';

    @Autowired
    private DishRepository dishRepository;

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MealTestData.LUNCH1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MealTestData.MEAL_MATCHER.contentJson(MealTestData.dish1));
    }

    @Test
    void getUnauth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MealTestData.LUNCH1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MealTestData.ADMIN_LUNCH_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + MealTestData.LUNCH1_ID))
                .andExpect(status().isNoContent());
        assertFalse(dishRepository.get(MealTestData.LUNCH1_ID, UserTestData.USER_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void deleteDataConflict() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + MealTestData.ADMIN_LUNCH_ID))
                .andExpect(status().isConflict());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void update() throws Exception {
        Dish updated = MealTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + MealTestData.LUNCH1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        MealTestData.MEAL_MATCHER.assertMatch(dishRepository.getById(MealTestData.LUNCH1_ID), updated);
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void createWithLocation() throws Exception {
        Dish newDish = MealTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDish)));

        Dish created = MealTestData.MEAL_MATCHER.readFromJson(action);
        int newId = created.id();
        newDish.setId(newId);
        MealTestData.MEAL_MATCHER.assertMatch(created, newDish);
        MealTestData.MEAL_MATCHER.assertMatch(dishRepository.getById(newId), newDish);
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MealTestData.MEAL_TO_MATCHER.contentJson(MealsUtil.getTos(MealTestData.dishes, UserTestData.user.getCaloriesPerDay())));
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getBetween() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "filter")
                .param("startDate", "2020-01-30").param("startTime", "07:00")
                .param("endDate", "2020-01-31").param("endTime", "11:00"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MealTestData.MEAL_TO_MATCHER.contentJson(MealsUtil.createTo(MealTestData.dish5, true), MealsUtil.createTo(MealTestData.dish1, false)));
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getBetweenAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "filter?startDate=&endTime="))
                .andExpect(status().isOk())
                .andExpect(MealTestData.MEAL_TO_MATCHER.contentJson(MealsUtil.getTos(MealTestData.dishes, UserTestData.user.getCaloriesPerDay())));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void createInvalid() throws Exception {
        Dish invalid = new Dish(null, null, "Dummy", 200);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void updateInvalid() throws Exception {
        Dish invalid = new Dish(MealTestData.LUNCH1_ID, null, null, 6000);
        perform(MockMvcRequestBuilders.put(REST_URL + MealTestData.LUNCH1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void updateHtmlUnsafe() throws Exception {
        Dish invalid = new Dish(MealTestData.LUNCH1_ID, LocalDate.now(), "<script>alert(123)</script>", 200);
        perform(MockMvcRequestBuilders.put(REST_URL + MealTestData.LUNCH1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void updateDuplicate() {
        Dish invalid = new Dish(MealTestData.LUNCH1_ID, MealTestData.dish2.getCreatedDate(), "Dummy", 200);
        assertThrows(Exception.class, () ->
                perform(MockMvcRequestBuilders.put(REST_URL + MealTestData.LUNCH1_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(invalid)))
                        .andDo(print())
        );
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void createDuplicate() {
        Dish invalid = new Dish(null, MealTestData.adminDish1.getCreatedDate(), "Dummy", 200);
        assertThrows(Exception.class, () ->
                perform(MockMvcRequestBuilders.post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(invalid)))
                        .andDo(print())
                        .andExpect(status().isUnprocessableEntity())
        );
    }
}