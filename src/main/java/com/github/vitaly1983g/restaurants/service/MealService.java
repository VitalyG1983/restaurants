package com.github.vitaly1983g.restaurants.service;

import com.github.vitaly1983g.restaurants.model.Dish;
import com.github.vitaly1983g.restaurants.repository.DishRepository;
import com.github.vitaly1983g.restaurants.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class MealService {
    private final DishRepository dishRepository;
    private final UserRepository userRepository;

    @Transactional
    public Dish save(Dish dish, int userId) {
        //dish.setRestaurant(userRepository.getById(restaurant));
        return dishRepository.save(dish);
    }
}
