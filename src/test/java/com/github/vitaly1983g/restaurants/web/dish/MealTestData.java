package com.github.vitaly1983g.restaurants.web.dish;

import com.github.vitaly1983g.restaurants.model.Menu;
import com.github.vitaly1983g.restaurants.to.LunchTo;
import com.github.vitaly1983g.restaurants.web.MatcherFactory;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static java.time.LocalDateTime.of;

public class MealTestData {
    public static final MatcherFactory.Matcher<Menu> MEAL_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Menu.class, "user");
    public static MatcherFactory.Matcher<LunchTo> MEAL_TO_MATCHER = MatcherFactory.usingEqualsComparator(LunchTo.class);

    public static final int MENU1_ID = 1;
    public static final int ADMIN_MENU_ID = 8;

    public static final Menu menu1 = new Menu(MENU1_ID, LocalDate.of(2020, Month.JANUARY, 30));
    public static final Menu menu2 = new Menu(MENU1_ID + 1, LocalDate.of(2020, Month.JANUARY, 30));
    public static final Menu menu3 = new Menu(MENU1_ID + 2, LocalDate.of(2020, Month.JANUARY, 30));
    public static final Menu menu4 = new Menu(MENU1_ID + 3, LocalDate.of(2020, Month.JANUARY, 31));
    public static final Menu menu5 = new Menu(MENU1_ID + 4, LocalDate.of(2020, Month.JANUARY, 31));
    public static final Menu menu6 = new Menu(MENU1_ID + 5, LocalDate.of(2020, Month.JANUARY, 31));
    public static final Menu menu7 = new Menu(MENU1_ID + 6, LocalDate.of(2020, Month.JANUARY, 31));
    public static final Menu adminMenu1 = new Menu(ADMIN_MENU_ID, LocalDate.of(2020, Month.JANUARY, 31));
    public static final Menu adminMenu2 = new Menu(ADMIN_MENU_ID + 1, LocalDate.of(2020, Month.JANUARY, 31));

    public static final List<Menu> menus = List.of(menu7, menu6, menu5, menu4, menu3, menu2, menu1);

    public static Menu getNew() {
        return new Menu(null, LocalDate.of(2020, Month.FEBRUARY, 1));
    }

    public static Menu getUpdated() {
        return new Menu(MENU1_ID, menu1.getMenuDate().plus(2, ChronoUnit.MINUTES));
    }
}
