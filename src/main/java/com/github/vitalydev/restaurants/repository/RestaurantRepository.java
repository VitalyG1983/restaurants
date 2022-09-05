package com.github.vitalydev.restaurants.repository;

import com.github.vitalydev.restaurants.error.DataConflictException;
import com.github.vitalydev.restaurants.model.Restaurant;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    default Restaurant checkExistence(int id) {
        return findById(id).orElseThrow(
                () -> new DataConflictException("Restaurant id=" + id + " doesn't exist in database"));
    }
}