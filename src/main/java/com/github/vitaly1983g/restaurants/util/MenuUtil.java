package com.github.vitaly1983g.restaurants.util;

import com.github.vitaly1983g.restaurants.model.Dish;
import com.github.vitaly1983g.restaurants.model.Menu;
import com.github.vitaly1983g.restaurants.model.Restaurant;
import com.github.vitaly1983g.restaurants.repository.DishRepository;
import com.github.vitaly1983g.restaurants.to.LunchTo;
import com.github.vitaly1983g.restaurants.to.MenuTo;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

@UtilityClass
public class MenuUtil {
    static final Comparator<MenuTo> MENU_TO_DATE_COMPARATOR = Comparator.comparing(MenuTo::getMenuDate);
    public static final String DATE_PATTERN = "yyyy-MM-dd";


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

    public static List<Menu> createFromTo(MenuTo menuTo, DishRepository dishRepository) {
        List<Menu> menus = new ArrayList<>();
        //List<Integer> dishIds = new ArrayList<>();
        menuTo.getDishes().forEach(dish -> {
           // dishIds.add(dish.id());
            menus.add(new Menu(null, menuTo.getMenuDate(), menuTo.getRestaurant(),
                    dishRepository.findById(dish.id()).get()));
        });
        //dishRepository.findById(dish.id()).get())));
        // dish)));
       // menus.dishRepository.getAllByIds( menuTo.getRestaurant().id(), dishIds);
        return menus;
    }

    public static MenuTo createMenuTo(LocalDate menuDate, int restId, List<Dish> dishes) {
        //return new MenuTo(1, menuDate, restId, dishes);
        return null;
    }

 /*   public static List<Dish> getListDishes(List<Menu> menus, LocalDate menuDate) {
        Map<LocalDate, List<Dish>> dishesOnDate = menus.stream()
                .collect(Collectors.groupingBy(Menu::getMenuDate, mapping(Menu::getDish, toList())));
        return dishesOnDate.size() != 0 ? dishesOnDate.get(menuDate) : null;
    }*/

    public static List<MenuTo> getAllMenuTosForRestaurant(List<Menu> menus, Restaurant restaurant) {
        Map<LocalDate, List<Dish>> dishesByDate = menus.stream()
                .collect(Collectors.groupingBy(Menu::getMenuDate, mapping(Menu::getDish, toList())));
        Set<Map.Entry<LocalDate, List<Dish>>> entries = dishesByDate.entrySet();
        List<MenuTo> menuTos = new ArrayList<>();
        AtomicInteger id = new AtomicInteger();
        entries.forEach(menu -> menuTos.add(new MenuTo(id.getAndIncrement(), menu.getKey(), restaurant, menu.getValue())));
        menuTos.sort(MENU_TO_DATE_COMPARATOR);
        return menuTos;
    }

    public static List<MenuTo> getMenuTosByDateForRestaurants(List<Menu> menus, LocalDate menuDate) {
        Map<Restaurant, List<Dish>> dishesByRest = menus.stream()
                .collect(Collectors.groupingBy(Menu::getRestaurant, mapping(Menu::getDish, toList())));
        Set<Map.Entry<Restaurant, List<Dish>>> entries = dishesByRest.entrySet();
        List<MenuTo> menuTos = new ArrayList<>();
        AtomicInteger id = new AtomicInteger();
        entries.forEach(menu -> menuTos.add(new MenuTo(id.getAndIncrement(), menuDate, menu.getKey(), menu.getValue())));
        menuTos.sort(MENU_TO_DATE_COMPARATOR);
        return menuTos;
    }

   /* public static List<MenuTo> getMenuTosAllRestaurants(List<Menu> menus) {
        Map<LocalDate, Map<Restaurant, List<Dish>>> dishesByDate = menus.stream()
                .collect(Collectors.groupingBy(Menu::getMenuDate, Collectors.groupingBy(Menu::getRestaurant, mapping(Menu::getDish, toList()))));
        Set<Map.Entry<LocalDate, Map<Restaurant, List<Dish>>>> entries = dishesByDate.entrySet();
        List<MenuTo> menuTos = new ArrayList<>();
        AtomicInteger id = new AtomicInteger();
        entries.forEach(menu -> menuTos.add(new MenuTo(id.getAndIncrement(), menu.getKey(), restId, menu.getValue())));
        menuTos.sort(MENU_TO_DATE_COMPARATOR);
        return menuTos;
    }*/
}
