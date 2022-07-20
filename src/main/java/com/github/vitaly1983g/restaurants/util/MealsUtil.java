package com.github.vitaly1983g.restaurants.util;

import com.github.vitaly1983g.restaurants.to.LunchTo;
import lombok.experimental.UtilityClass;
import com.github.vitaly1983g.restaurants.model.Menu;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@UtilityClass
public class MealsUtil {

    public static List<LunchTo> getTos(Collection<Menu> menus, int caloriesPerDay) {
        return filterByPredicate(menus, caloriesPerDay, meal -> true);
    }

  /*  public static List<MealTo> getFilteredTos(Collection<Lunch> lunches, int caloriesPerDay, LocalTime startTime, LocalTime endTime) {
        return filterByPredicate(lunches, caloriesPerDay, dish -> Util.isBetweenHalfOpen(dish.getTime(), startTime, endTime));
    }*/

    public static List<LunchTo> filterByPredicate(Collection<Menu> menus, int caloriesPerDay, Predicate<Menu> filter) {
        Map<LocalDate, Integer> caloriesSumByDate = menus.stream()
                .collect(
                        Collectors.groupingBy(Menu::getMenuDate, Collectors.summingInt(Menu::getId))
//    или                  Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );

        return menus.stream()
                .filter(filter)
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getMenuDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static LunchTo createTo(Menu menu, boolean excess) {
        return new LunchTo(menu.getId(), menu.getMenuDate(), menu.toString(), menu.id(), excess);
    }
}
