package com.github.vitaly1983g.restaurants.repository;

import com.github.vitaly1983g.restaurants.error.DataConflictException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import com.github.vitaly1983g.restaurants.model.Menu;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Menu> {

    @Query("SELECT d FROM Menu d WHERE d.restaurant.id=:userId ORDER BY d.menuDate DESC")
    List<Menu> getAll(int userId);

    @Query("SELECT m from Menu m WHERE m.restaurant.id=:userId AND m.menuDate >= :startDate AND m.menuDate < :endDate ORDER BY m.menuDate DESC")
    List<Menu> getBetweenHalfOpen(LocalDateTime startDate, LocalDateTime endDate, int userId);

    @Query("SELECT m FROM Menu m WHERE m.id = :id and m.restaurant.id = :userId")
    Optional<Menu> get(int id, int userId);

    @Query("SELECT m FROM Menu m JOIN FETCH m.restaurant WHERE m.id = :id and m.restaurant.id = :userId")
    Optional<Menu> getWithUser(int id, int userId);

    default Menu checkBelong(int id, int userId) {
        return get(id, userId).orElseThrow(
                () -> new DataConflictException("Dish id=" + id + " doesn't belong to Restaurant id=" + userId));
    }
}