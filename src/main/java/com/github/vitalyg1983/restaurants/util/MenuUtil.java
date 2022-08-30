package com.github.vitalyg1983.restaurants.util;

import com.github.vitalyg1983.restaurants.model.Dish;
import com.github.vitalyg1983.restaurants.model.DishInMenu;
import com.github.vitalyg1983.restaurants.model.Menu;
import com.github.vitalyg1983.restaurants.model.Restaurant;
import com.github.vitalyg1983.restaurants.to.MenuTo;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class MenuUtil {

    public static Menu create(Restaurant restaurant, List<Dish> dishes, LocalDate menuDate) {
        List<DishInMenu> menus = createDishInMenu(dishes);
        return new Menu(null, menuDate, restaurant, menus);
    }

    public static List<DishInMenu> createDishInMenu(List<Dish> dishes) {
        List<DishInMenu> menus = new ArrayList<>();
        dishes.forEach(dish -> menus.add(new DishInMenu(dish)));
        return menus;
    }

/*    public static List<MenuTo> createTos(List<Menu> menus) {
        List<MenuTo> menuTos = new ArrayList<>();
        menus.forEach(menu -> menuTos.add(new MenuTo(menu)));
        return new Menu(null, menuDate, restaurant, menuTos);
    }*/
}
