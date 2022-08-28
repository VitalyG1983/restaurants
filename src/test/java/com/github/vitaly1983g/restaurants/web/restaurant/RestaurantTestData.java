package com.github.vitaly1983g.restaurants.web.restaurant;

import com.github.vitaly1983g.restaurants.model.Restaurant;
import com.github.vitaly1983g.restaurants.web.MatcherFactory;

import java.util.List;

import static com.github.vitaly1983g.restaurants.web.dish.DishTestData.rest1Dishes;

public class RestaurantTestData {
    public static MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingEqualsComparator(Restaurant.class);

    public static final int REST_ID1 = 1;
    public static final int REST_ID2 = 2;
    public static final int INVALID_RESTAURANT_ID = 99;

    public static final Restaurant rest1 = new Restaurant(REST_ID1, "Токио City", "Просвещения 72");
    public static final Restaurant rest2 = new Restaurant(REST_ID1 + 1, "BAHROMA", "Просвещения 48");
    public static final Restaurant rest3 = new Restaurant(REST_ID1 + 2, "Бургер Кинг", "Энгельса 154");
    public static final Restaurant rest4 = new Restaurant(REST_ID1 + 3, "Додо Пицца", "Художников 26");

    public static final List<Restaurant> restaurants = List.of(rest2, rest3, rest4, rest1);

    static {
        rest1.setDishes(rest1Dishes);
    }

    public static Restaurant getNew() {
        return new Restaurant(null, "Новый ресторан", "Новый адрес, 99");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(REST_ID1, "Обновленный ресторан", "Обновленный адрес");
    }
}
