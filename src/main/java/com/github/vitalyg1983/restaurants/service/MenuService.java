package com.github.vitalyg1983.restaurants.service;

import com.github.vitalyg1983.restaurants.error.DataConflictException;
import com.github.vitalyg1983.restaurants.model.Dish;
import com.github.vitalyg1983.restaurants.model.DishInMenu;
import com.github.vitalyg1983.restaurants.model.Menu;
import com.github.vitalyg1983.restaurants.repository.DishRepository;
import com.github.vitalyg1983.restaurants.repository.MenuRepository;
import com.github.vitalyg1983.restaurants.repository.RestaurantRepository;
import com.github.vitalyg1983.restaurants.to.MenuTo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.github.vitalyg1983.restaurants.util.MenuUtil.create;
import static com.github.vitalyg1983.restaurants.util.MenuUtil.createDishInMenu;

@Service
@AllArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;

    public Menu save(MenuTo menuTo, int restId, LocalDate menuDate, int... menuId) {
        Set<Integer> dishIds = new HashSet<>(menuTo.getDishIds());
        Set<Integer> idsRepository = dishRepository.getAllIds(restId, dishIds);
        dishIds.removeAll(idsRepository);
        if (dishIds.size() != 0) {
            throw new DataConflictException("Dishes with ids=" + dishIds + " doesn't belong to Restaurant id=" + restId);
        }
        if (menuId.length != 0) {
            List<Dish> createdDishesInMenu = createDishInMenu(dishRepository.findAllById(idsRepository));
            Menu menu = menuRepository.getById(menuId[0]);
            List<Dish> dishesInMenu = menu.getDishesInMenu();
            // here Hibernate directly clear the rows of dishesInMenu from the DB table DISHINMENU
            dishesInMenu.clear();
            menuRepository.flush();
            // here Hibernate directly appends the rows of dishesInMenu to the DB table DISHINMENU
            dishesInMenu.addAll(createdDishesInMenu);
            return null;
        } else {
            return menuRepository.save(create(restaurantRepository.findById(restId).get(),
                    dishRepository.findAllById(idsRepository), menuDate));
        }
    }
}