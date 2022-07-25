package com.github.vitaly1983g.restaurants.service;

import com.github.vitaly1983g.restaurants.model.Dish;
import com.github.vitaly1983g.restaurants.model.Menu;
import com.github.vitaly1983g.restaurants.repository.DishRepository;
import com.github.vitaly1983g.restaurants.repository.MenuRepository;
import com.github.vitaly1983g.restaurants.to.MenuTo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.github.vitaly1983g.restaurants.util.MenuUtil.createFromTo;

@Service
@AllArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final DishRepository dishRepository;

    @Transactional
    public List<Menu> save(MenuTo menuTo, int restId) {
        menuTo.setRestId(restId);
        return menuRepository.saveAll(createFromTo(menuTo));
    }

    public Dish save(Dish dish, int restId) {
        dish.setRestId(restId);
        return dishRepository.save(dish);
    }
}
