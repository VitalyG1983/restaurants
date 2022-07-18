package com.github.vitaly1983g.restaurants.util;

import com.github.vitaly1983g.restaurants.to.LunchTo;
import lombok.experimental.UtilityClass;
import com.github.vitaly1983g.restaurants.model.Dish;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@UtilityClass
public class MealsUtil {

    public static List<LunchTo> getTos(Collection<Dish> dishes, int caloriesPerDay) {
        return filterByPredicate(dishes, caloriesPerDay, meal -> true);
    }

  /*  public static List<MealTo> getFilteredTos(Collection<Lunch> lunches, int caloriesPerDay, LocalTime startTime, LocalTime endTime) {
        return filterByPredicate(lunches, caloriesPerDay, meal -> Util.isBetweenHalfOpen(meal.getTime(), startTime, endTime));
    }*/

    public static List<LunchTo> filterByPredicate(Collection<Dish> dishes, int caloriesPerDay, Predicate<Dish> filter) {
        Map<LocalDate, Integer> caloriesSumByDate = dishes.stream()
                .collect(
                        Collectors.groupingBy(Dish::getCreatedDate, Collectors.summingInt(Dish::getPrice))
//    или                  Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );

        return dishes.stream()
                .filter(filter)
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getCreatedDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static LunchTo createTo(Dish dish, boolean excess) {
        return new LunchTo(dish.getId(), dish.getCreatedDate(), dish.getName(), dish.getPrice(), excess);
    }
}
