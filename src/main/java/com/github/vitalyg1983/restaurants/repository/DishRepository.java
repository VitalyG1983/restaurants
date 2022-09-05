package com.github.vitalyg1983.restaurants.repository;

import com.github.vitalyg1983.restaurants.error.DataConflictException;
import com.github.vitalyg1983.restaurants.model.Dish;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {

    @Query("SELECT d FROM Dish d WHERE d.restId=:restId ORDER BY d.name")
    List<Dish> getAll(int restId);

    @Query("SELECT d.id FROM Dish d WHERE d.restId=:restId AND d.id IN (:ids)")
    Set<Integer> getAllIds(int restId, Set<Integer> ids);

    @Query("SELECT d FROM Dish d WHERE d.id = :id and d.restId = :restId")
    Optional<Dish> get(int id, int restId);

    default Dish checkBelong(int id, int restId) {
        return get(id, restId).orElseThrow(
                () -> new DataConflictException("Dish id=" + id + " doesn't belong to Restaurant id=" + restId));
    }
}