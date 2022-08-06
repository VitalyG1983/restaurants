package com.github.vitaly1983g.restaurants.service;

import com.github.vitaly1983g.restaurants.error.DataConflictException;
import com.github.vitaly1983g.restaurants.model.Menu;
import com.github.vitaly1983g.restaurants.model.Restaurant;
import com.github.vitaly1983g.restaurants.repository.DishRepository;
import com.github.vitaly1983g.restaurants.repository.MenuRepository;
import com.github.vitaly1983g.restaurants.repository.RestaurantRepository;
import com.github.vitaly1983g.restaurants.to.MenuTo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.github.vitaly1983g.restaurants.util.MenuUtil.createFromTo;

@Service
@AllArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public Menu save(MenuTo menuTo, int restId, LocalDate menuDate) {
        //Restaurant restaurant = getRestaurant(restId);
        //menuTo.getDishIds().forEach(dish -> dishRepository.checkBelong(dish.id(), restId));
        Set<Integer> dishIds = new HashSet<>(menuTo.getDishIds());
        Set<Integer> idsRepository = dishRepository.getAllIds(restId, dishIds);
        dishIds.removeAll(idsRepository);
        if (dishIds.size() != 0) {
            throw new DataConflictException("Dishes with ids=" + dishIds + " doesn't belong to Restaurant id=" + restId);
        }
        menuTo.setMenuDate(menuDate);
        return menuRepository.save(createFromTo(menuTo, restaurantRepository.getById(restId), dishRepository.findAllById(idsRepository)));
    }

/*    public List<Menu> getAll(int restId) {
        Restaurant restaurant = getRestaurant(restId);
        List<Menu> all = menuRepository.getAll(restId);
        //return MenuUtil.getAllMenuTosForRestaurant(all, restId);
        return menuRepository.getAll(restId);
    }*/

    public Restaurant getRestaurant(int restId) {
        return restaurantRepository.findById(restId).orElseThrow(
                () -> new EntityNotFoundException("Restaurant id=" + restId + " not found in DB"));
    }
}
