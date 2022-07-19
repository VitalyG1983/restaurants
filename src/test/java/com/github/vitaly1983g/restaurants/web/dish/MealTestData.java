package com.github.vitaly1983g.restaurants.web.dish;

import com.github.vitaly1983g.restaurants.model.Dish;
import com.github.vitaly1983g.restaurants.to.LunchTo;
import com.github.vitaly1983g.restaurants.web.MatcherFactory;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static java.time.LocalDateTime.of;

public class MealTestData {
    public static final MatcherFactory.Matcher<Dish> MEAL_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Dish.class, "user");
    public static MatcherFactory.Matcher<LunchTo> MEAL_TO_MATCHER = MatcherFactory.usingEqualsComparator(LunchTo.class);

    public static final int LUNCH1_ID = 1;
    public static final int ADMIN_LUNCH_ID = 8;

    public static final Dish dish1 = new Dish(LUNCH1_ID, LocalDate.of(2020, Month.JANUARY, 30), "Завтрак", 500);
    public static final Dish dish2 = new Dish(LUNCH1_ID + 1, LocalDate.of(2020, Month.JANUARY, 30), "Обед", 1000);
    public static final Dish dish3 = new Dish(LUNCH1_ID + 2, LocalDate.of(2020, Month.JANUARY, 30), "Ужин", 500);
    public static final Dish dish4 = new Dish(LUNCH1_ID + 3, LocalDate.of(2020, Month.JANUARY, 31), "Еда на граничное значение", 100);
    public static final Dish dish5 = new Dish(LUNCH1_ID + 4, LocalDate.of(2020, Month.JANUARY, 31), "Завтрак", 500);
    public static final Dish dish6 = new Dish(LUNCH1_ID + 5, LocalDate.of(2020, Month.JANUARY, 31), "Обед", 1000);
    public static final Dish dish7 = new Dish(LUNCH1_ID + 6, LocalDate.of(2020, Month.JANUARY, 31), "Ужин", 510);
    public static final Dish adminDish1 = new Dish(ADMIN_LUNCH_ID, LocalDate.of(2020, Month.JANUARY, 31), "Админ ланч", 510);
    public static final Dish adminDish2 = new Dish(ADMIN_LUNCH_ID + 1, LocalDate.of(2020, Month.JANUARY, 31), "Админ ужин", 1500);

    public static final List<Dish> dishes = List.of(dish7, dish6, dish5, dish4, dish3, dish2, dish1);

    public static Dish getNew() {
        return new Dish(null, LocalDate.of(2020, Month.FEBRUARY, 1), "Созданный ужин", 300);
    }

    public static Dish getUpdated() {
        return new Dish(LUNCH1_ID, dish1.getCreatedDate().plus(2, ChronoUnit.MINUTES), "Обновленный завтрак", 200);
    }
}
