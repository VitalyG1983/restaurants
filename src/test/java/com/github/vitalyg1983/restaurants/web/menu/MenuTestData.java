package com.github.vitalyg1983.restaurants.web.menu;

import com.github.vitalyg1983.restaurants.model.Dish;
import com.github.vitalyg1983.restaurants.model.Menu;
import com.github.vitalyg1983.restaurants.to.MenuTo;
import com.github.vitalyg1983.restaurants.util.DateTimeUtil;
import com.github.vitalyg1983.restaurants.web.MatcherFactory;
import com.github.vitalyg1983.restaurants.web.restaurant.RestaurantTestData;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.List;

import static com.github.vitalyg1983.restaurants.web.dish.DishTestData.*;

public class MenuTestData {
    public static final MatcherFactory.Matcher<Menu> MENU_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Menu.class, "restaurant.dishes", "restaurant.votes", "dishesInMenu.id");

    public static final int MENU1_ID = 1;
    public static final int REST2_MENU1_ID = 4;
    public static final int INVALID_MENU_ID = 99;
    public static final String NEW_DATE = DateTimeUtil.NOW_DATE.plusDays(1).toString();

    //public static final Menu emptyRestMenu1 = new Menu(MENU1_ID, NOW_DATE);
    public static final Menu rest1Menu1 = new Menu(MENU1_ID, DateTimeUtil.NOW_DATE, RestaurantTestData.rest1);
    public static final Menu rest1Menu2 = new Menu(MENU1_ID + 1, LocalDate.of(2020, Month.JANUARY, 31), RestaurantTestData.rest1);
    public static final Menu rest1Menu3 = new Menu(MENU1_ID + 2, LocalDate.of(2020, Month.JANUARY, 29), RestaurantTestData.rest1);
    public static final Menu rest2Menu1 = new Menu(REST2_MENU1_ID, DateTimeUtil.NOW_DATE, RestaurantTestData.rest2);
    public static final Menu rest3Menu1 = new Menu(MENU1_ID + 4, LocalDate.of(2020, Month.JANUARY, 31), RestaurantTestData.rest3);

    // public static final Dish dish2 = new Dish(dish2);
    // public static final Dish dish3 = new Dish(dish3);
    //  public static final Dish dish1 = new Dish(dish1);

    //public static final Dish rest2Dish4 = new Dish(rest2Dish4);
    //public static final Dish rest2Dish5 = new Dish(rest2Dish5);
    // public static final Dish rest2Dish6 = new Dish(rest2Dish6);

    //public static final Dish dish7 = new Dish(dish7);
    //public static final Dish dish8 = new Dish(dish8);

    public static final List<Menu> rest1Menus = List.of(rest1Menu3, rest1Menu2, rest1Menu1);
    public static final List<Menu> restMenusOnDate = List.of(rest1Menu1, rest2Menu1);
    public static final List<Dish> dishes1InMenu1 = List.of(dish1, dish2, dish3);
    public static final List<Dish> dishes2InMenu1 = List.of(dish1, dish2);

    static {
        rest1Menu1.setDishesInMenu(List.of(dish1));
        rest1Menu2.setDishesInMenu(List.of(dish2));
        rest1Menu3.setDishesInMenu(List.of(dish3));
        rest2Menu1.setDishesInMenu(List.of(rest2Dish6, rest2Dish4, rest2Dish5));
        rest3Menu1.setDishesInMenu(List.of(dish7, dish8));
    }

    public static Menu getNew() {
        return new Menu(null, LocalDate.of(2020, Month.FEBRUARY, 1), RestaurantTestData.rest1, dishes1InMenu1);
    }

    public static MenuTo getNewMenuTo() {
        return new MenuTo(null, new HashSet<>(List.of(2, 3, 1)));
    }

    public static Menu getUpdated() {
        return new Menu(MENU1_ID, rest1Menu1.getMenuDate(), RestaurantTestData.rest1, dishes2InMenu1);
    }
}
