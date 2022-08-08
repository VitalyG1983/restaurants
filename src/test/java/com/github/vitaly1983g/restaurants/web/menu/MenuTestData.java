package com.github.vitaly1983g.restaurants.web.menu;

import com.github.vitaly1983g.restaurants.model.DishInMenu;
import com.github.vitaly1983g.restaurants.model.Menu;
import com.github.vitaly1983g.restaurants.to.MenuTo;
import com.github.vitaly1983g.restaurants.web.MatcherFactory;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.List;

import static com.github.vitaly1983g.restaurants.web.dish.DishTestData.*;
import static com.github.vitaly1983g.restaurants.web.restaurant.RestaurantTestData.*;
import static org.assertj.core.api.Assertions.assertThat;

public class MenuTestData {
    public static final MatcherFactory.Matcher<Menu> MENU_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Menu.class, "restaurant.dishes", "restaurant.votes", "dishesInMenu.id");
    //public static MatcherFactory.Matcher<MenuTo> MENU_TO_MATCHER = MatcherFactory.usingEqualsComparator(MenuTo.class);
    public static MatcherFactory.Matcher<Menu> MENU_WITH_DISHES_MATCHER =
            MatcherFactory.usingAssertions(Menu.class,
                    //     No need use ignoringAllOverriddenEquals, see https://assertj.github.io/doc/#breaking-changes
                    (a, e) -> assertThat(a).usingRecursiveComparison().isEqualTo(e),
                    (a, e) -> {
                        throw new UnsupportedOperationException();
                    });

    public static final int MENU1_ID = 1;
    public static final int REST2_MENU1_ID = 4;
    public static final String NEW_DATE = " 2020-02-01";

    //public static final int ADMIN_MENU_ID = 8;

    public static final Menu rest1Menu1 = new Menu(MENU1_ID, LocalDate.of(2020, Month.JANUARY, 30), rest1);
    public static final Menu rest1Menu2 = new Menu(MENU1_ID + 1, LocalDate.of(2020, Month.JANUARY, 31), rest1);
    public static final Menu rest1Menu3 = new Menu(MENU1_ID + 2, LocalDate.of(2020, Month.JANUARY, 29), rest1);
    public static final Menu rest2Menu1 = new Menu(REST2_MENU1_ID, LocalDate.of(2020, Month.JANUARY, 31), rest2);
    public static final Menu rest3Menu1 = new Menu(MENU1_ID + 4, LocalDate.of(2020, Month.JANUARY, 31), rest3);

    public static final DishInMenu dishInMenu1 = new DishInMenu(1, dish1);
    public static final DishInMenu dishInMenu2 = new DishInMenu(2 ,dish2);
    public static final DishInMenu dishInMenu3 = new DishInMenu(3, dish3);


    // public static final Menu adminMenu1 = new Menu(ADMIN_MENU_ID, of(2020, Month.JANUARY, 31, 14, 0), "Админ ланч", 510);
    // public static final Menu adminMenu2 = new Menu(ADMIN_MENU_ID + 1, of(2020, Month.JANUARY, 31, 21, 0), "Админ ужин", 1500);

    public static final List<Menu> menus = List.of(rest3Menu1, rest2Menu1, rest1Menu3, rest1Menu2, rest1Menu1);
    public static final List<DishInMenu> dishes1InMenu1 = List.of(dishInMenu3, dishInMenu2, dishInMenu1);
    public static final List<DishInMenu> dishes2InMenu1 = List.of(dishInMenu1, dishInMenu2);

    static {
        rest1Menu1.setDishesInMenu(dishes1InMenu1);
    }

    public static Menu getNew() {
        return new Menu(null, LocalDate.of(2020, Month.FEBRUARY, 1), rest1, dishes1InMenu1);
    }

    public static MenuTo getNewMenuTo() {
        return new MenuTo(null, new HashSet<>(List.of(2, 1)));
    }

    public static Menu getUpdated() {
        return new Menu(MENU1_ID, rest1Menu1.getMenuDate(), rest1, dishes2InMenu1);
    }
}
