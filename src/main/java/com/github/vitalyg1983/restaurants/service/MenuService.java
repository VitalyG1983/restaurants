package com.github.vitalyg1983.restaurants.service;

import com.github.vitalyg1983.restaurants.error.DataConflictException;
import com.github.vitalyg1983.restaurants.model.DishInMenu;
import com.github.vitalyg1983.restaurants.model.Menu;
import com.github.vitalyg1983.restaurants.repository.DishRepository;
import com.github.vitalyg1983.restaurants.repository.MenuRepository;
import com.github.vitalyg1983.restaurants.repository.RestaurantRepository;
import com.github.vitalyg1983.restaurants.to.MenuTo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.github.vitalyg1983.restaurants.util.MenuUtil.createDishesInMenu;
import static com.github.vitalyg1983.restaurants.util.MenuUtil.createMenu;

@Service
@AllArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public void update(MenuTo menuTo, int restId, int menuId) {
        Menu menu = menuRepository.checkBelong(menuId, restId);
        List<DishInMenu> createdDishesInMenu = createDishesInMenu(dishRepository.findAllById(getIds(menuTo, restId)));
        List<DishInMenu> dishesInMenu = menu.getDishesInMenu();
        // here Hibernate directly clear the rows of dishesInMenu from the DB table DISHINMENU
        dishesInMenu.clear();
        menuRepository.flush();
        // here Hibernate directly appends the rows of dishesInMenu to the DB table DISHINMENU
        dishesInMenu.addAll(createdDishesInMenu);
    }

    @Transactional
    public Menu create(MenuTo menuTo, int restId, LocalDate menuDate) {
        return menuRepository.save(createMenu(restaurantRepository.checkExistence(restId),
                dishRepository.findAllById(getIds(menuTo, restId)), menuDate));
    }

    public Set<Integer> getIds(MenuTo menuTo, int restId) {
        Set<Integer> dishIds = new HashSet<>(menuTo.getDishIds());
        Set<Integer> idsRepository = dishRepository.getAllIds(restId, dishIds);
        dishIds.removeAll(idsRepository);
        if (dishIds.size() != 0) {
            throw new DataConflictException("Dishes with ids=" + dishIds + " doesn't belong to Restaurant id=" + restId);
        }
        return idsRepository;
    }
}