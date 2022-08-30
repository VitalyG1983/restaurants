package com.github.vitalyg1983.restaurants.repository;

import com.github.vitalyg1983.restaurants.error.DataConflictException;
import com.github.vitalyg1983.restaurants.model.Restaurant;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    default Restaurant checkExistence(int id) {
        return findById(id).orElseThrow(
                () -> new DataConflictException("Restaurant id=" + id + " doesn't exist in database"));
    }
}