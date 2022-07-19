package com.github.vitaly1983g.restaurants.repository;

import com.github.vitaly1983g.restaurants.error.DataConflictException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import com.github.vitaly1983g.restaurants.model.Dish;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id=:userId ORDER BY d.createdDate DESC")
    List<Dish> getAll(int userId);

    @Query("SELECT m from Dish m WHERE m.restaurant.id=:userId AND m.createdDate >= :startDate AND m.createdDate < :endDate ORDER BY m.createdDate DESC")
    List<Dish> getBetweenHalfOpen(LocalDateTime startDate, LocalDateTime endDate, int userId);

    @Query("SELECT m FROM Dish m WHERE m.id = :id and m.restaurant.id = :userId")
    Optional<Dish> get(int id, int userId);

    @Query("SELECT m FROM Dish m JOIN FETCH m.restaurant WHERE m.id = :id and m.restaurant.id = :userId")
    Optional<Dish> getWithUser(int id, int userId);

    default Dish checkBelong(int id, int userId) {
        return get(id, userId).orElseThrow(
                () -> new DataConflictException("Dish id=" + id + " doesn't belong to Restaurant id=" + userId));
    }
}