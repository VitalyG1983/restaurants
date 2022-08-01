package com.github.vitaly1983g.restaurants.service;

import com.github.vitaly1983g.restaurants.model.Restaurant;
import com.github.vitaly1983g.restaurants.repository.DishRepository;
import com.github.vitaly1983g.restaurants.repository.MenuRepository;
import com.github.vitaly1983g.restaurants.repository.RestaurantRepository;
import com.github.vitaly1983g.restaurants.to.MenuTo;
import com.github.vitaly1983g.restaurants.model.Menu;
import com.github.vitaly1983g.restaurants.util.MenuUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

import static com.github.vitaly1983g.restaurants.util.MenuUtil.createFromTo;

@Service
@AllArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public List<Menu> save(MenuTo menuTo, int restId, LocalDate menuDate) {
        menuTo.getDishes().forEach(dish -> dishRepository.checkBelong(dish.id(), restId));
        menuTo.setRestaurant(restaurantRepository.findById(restId).get());
        menuTo.setMenuDate(menuDate);
        return menuRepository.saveAll(createFromTo(menuTo, dishRepository));
    }

    public List<MenuTo> getAll(int restId) {
        Restaurant restaurant = restaurantRepository.findById(restId).orElseThrow(
                () -> new EntityNotFoundException("Restaurant id=" + restId + " not found in DB"));
        List<Menu> all = menuRepository.getAll(restId);
        return MenuUtil.getAllMenuTosForRestaurant(all, restaurant);
    }
}
