package com.github.vitaly1983g.restaurants.web.dish;

import com.github.vitaly1983g.restaurants.model.Dish;
import com.github.vitaly1983g.restaurants.web.MatcherFactory;

import java.util.List;

public class DishTestData {
    public static MatcherFactory.Matcher<Dish> DISH_MATCHER = MatcherFactory.usingEqualsComparator(Dish.class);

    public static final int DISH1_ID = 1;
    public static final int REST2_DISH4_ID = 4;


    public static final Dish dish1 = new Dish(DISH1_ID, "Мисо суп", 330, 1);
    public static final Dish dish2 = new Dish(DISH1_ID + 1, "Ролл Филадельфия", 1000, 1);
    public static final Dish dish3 = new Dish(DISH1_ID + 2, "Пуэр", 500, 1);
    public static final Dish rest2Dish4 = new Dish(REST2_DISH4_ID, "Домашний супчик", 400, 2);
    public static final Dish rest2Dish5 = new Dish(REST2_DISH4_ID + 1, "Плов узбекский", 250, 2);
    public static final Dish rest2Dish6 = new Dish(REST2_DISH4_ID + 2, "Бургер из говядины", 200, 2);
    public static final Dish dish7 = new Dish(REST2_DISH4_ID + 3, "Гриль Кинг", 350, 3);
    public static final Dish dish8 = new Dish(REST2_DISH4_ID + 4, "Кола", 100, 3);
    /* public static final Dish adminDish1 = new Dish(ADMIN_MENU_ID, LocalDate.of(2020, Month.JANUARY, 31));
     public static final Dish adminDish2 = new Dish(ADMIN_MENU_ID + 1, LocalDate.of(2020, Month.JANUARY, 31));
 */
    public static final List<Dish> dishes = List.of(dish8, dish7, rest2Dish6, rest2Dish5, rest2Dish4, dish3, dish2, dish1);
    public static final List<Dish> rest1Dishes = List.of(dish1, dish3, dish2);

    public static Dish getNew() {
        return new Dish(null, "Новая еда", 99, 1);
    }

    public static Dish getUpdated() {
        return new Dish(DISH1_ID, "Обновленная еда", 100, 4);
    }
}
