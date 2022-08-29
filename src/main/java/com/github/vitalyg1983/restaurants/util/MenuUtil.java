package com.github.vitalyg1983.restaurants.util;

import com.github.vitalyg1983.restaurants.model.Dish;
import com.github.vitalyg1983.restaurants.model.Menu;
import com.github.vitalyg1983.restaurants.model.Restaurant;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class MenuUtil {

    public static Menu create(Restaurant restaurant, List<Dish> dishes, LocalDate menuDate) {
        List<Dish> menus = createDishInMenu(dishes);
        return new Menu(null, menuDate, restaurant, menus);
    }

    public static List<Dish> createDishInMenu(List<Dish> dishes) {
        List<Dish> menus = new ArrayList<>();
        dishes.forEach(dish -> menus.add(new Dish(dish)));
        return menus;
    }
}
