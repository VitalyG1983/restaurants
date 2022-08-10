package com.github.vitaly1983g.restaurants.util;

import com.github.vitaly1983g.restaurants.model.Dish;
import com.github.vitaly1983g.restaurants.model.DishInMenu;
import com.github.vitaly1983g.restaurants.model.Menu;
import com.github.vitaly1983g.restaurants.model.Restaurant;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class MenuUtil {

    public static Menu create(Restaurant restaurant, List<Dish> dishes, LocalDate menuDate) {
        List<DishInMenu> menus = new ArrayList<>();
        dishes.forEach(dish -> menus.add(new DishInMenu(null, dish)));
        return new Menu(null, menuDate, restaurant, menus);
    }
}
