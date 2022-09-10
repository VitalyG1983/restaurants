package com.github.vitalydev.restaurants.web.menu;

import com.github.vitalydev.restaurants.model.Dish;
import com.github.vitalydev.restaurants.model.Menu;
import com.github.vitalydev.restaurants.to.MenuTo;
import com.github.vitalydev.restaurants.web.MatcherFactory;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.List;

import static com.github.vitalydev.restaurants.web.dish.DishTestData.*;

public class MenuTestData {
   // public static final MatcherFactory.Matcher<Menu> MENU_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Menu.class, "restaurant.dishes", "restaurant.votes", "dishesInMenu.id", "dishesInMenu.dish.restId");
    public static final MatcherFactory.Matcher<Menu> MENU_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Menu.class, "restaurant", "dishesInMenu.id", "dishesInMenu.restId");

    public static final int MENU1_ID = 1;
    public static final int REST2_MENU1_ID = 4;
    public static final int INVALID_MENU_ID = 99;
    public static final String NEW_DATE = LocalDate.now().plusDays(1).toString();

    //public static final Menu emptyRestMenu1 = new Menu(MENU1_ID, NOW_DATE);
    public static final Menu rest1Menu1 = new Menu(MENU1_ID, LocalDate.now(), null); //RestaurantTestData.rest1);
    public static final Menu rest1Menu2 = new Menu(MENU1_ID + 1, LocalDate.of(2020, Month.JANUARY, 31), null);// RestaurantTestData.rest1);
    public static final Menu rest1Menu3 = new Menu(MENU1_ID + 2, LocalDate.of(2020, Month.JANUARY, 29), null);// RestaurantTestData.rest1);
    public static final Menu rest2Menu1 = new Menu(REST2_MENU1_ID, LocalDate.now(), null);//, RestaurantTestData.rest2);
    public static final Menu rest3Menu1 = new Menu(MENU1_ID + 4, LocalDate.of(2020, Month.JANUARY, 31), null); //RestaurantTestData.rest3);

 /*   public static final DishInMenu dish2InMenu = new DishInMenu(dish2);
    public static final DishInMenu dish3InMenu = new DishInMenu(dish3);
    public static final DishInMenu dish1InMenu = new DishInMenu(dish1);*/

 /*   public static final DishInMenu dish4InMenu4 = new DishInMenu(DishTestData.rest2Dish4);
    public static final DishInMenu dish5InMenu4 = new DishInMenu(DishTestData.rest2Dish5);
    public static final DishInMenu dish6InMenu4 = new DishInMenu(DishTestData.rest2Dish6);*/

  /*  public static final DishInMenu dish7InMenu5 = new DishInMenu(DishTestData.dish7);
    public static final DishInMenu dish8InMenu5 = new DishInMenu(DishTestData.dish8);*/

    public static final List<Menu> rest1Menus = List.of(rest1Menu1, rest1Menu2, rest1Menu3);
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
        return new Menu(null, LocalDate.of(2020, Month.FEBRUARY, 1), null,//RestaurantTestData.rest1,
                List.of(dish1, dish2, dish3));
    }

    public static MenuTo getNewMenuTo() {
        return new MenuTo(null, new HashSet<>(List.of(2, 3, 1)), LocalDate.now());
    }

    public static Menu getUpdated() {
        return new Menu(MENU1_ID, rest1Menu1.getMenuDate(), null, //RestaurantTestData.rest1,
                List.of(dish1, dish2));
    }
}
