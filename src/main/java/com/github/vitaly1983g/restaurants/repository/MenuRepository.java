package com.github.vitaly1983g.restaurants.repository;

import com.github.vitaly1983g.restaurants.error.DataConflictException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import com.github.vitaly1983g.restaurants.model.Menu;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuRepository extends BaseRepository<Menu> {

    @Query("SELECT m FROM Menu m WHERE m.restaurant.id=:restId ORDER BY m.menuDate")
    List<Menu> getAll(int restId);

    @Query("SELECT m FROM Menu m WHERE m.id = :id and m.restaurant.id = :restId")
    Optional<Menu> get(int id, int restId);

    @Query("SELECT m FROM Menu m WHERE m.menuDate = :menuDate and m.restaurant.id = :restId")
    Optional<Menu> getByDate(LocalDate menuDate, int restId);

    @Query("SELECT m from Menu m WHERE m.restaurant.id=:userId AND m.menuDate >= :startDate AND m.menuDate < :endDate ORDER BY m.menuDate DESC")
    List<Menu> getBetweenHalfOpen(LocalDateTime startDate, LocalDateTime endDate, int userId);


    @Query("SELECT m FROM Menu m JOIN FETCH m.restaurant WHERE m.id = :id and m.restaurant.id = :userId")
    Optional<Menu> getWithUser(int id, int userId);

    default Menu checkBelong(int id, int userId) {
        return get(id, userId).orElseThrow(
                () -> new DataConflictException("Dish id=" + id + " doesn't belong to Restaurant id=" + userId));
    }
}