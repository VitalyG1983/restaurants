package com.github.vitaly1983g.restaurants.service;

import com.github.vitaly1983g.restaurants.model.Menu;
import com.github.vitaly1983g.restaurants.repository.MenuRepository;
import com.github.vitaly1983g.restaurants.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class MealService {
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;

    @Transactional
    public Menu save(Menu menu, int userId) {
        //dish.setRestaurant(userRepository.getById(restaurant));
        return menuRepository.save(menu);
    }
}
