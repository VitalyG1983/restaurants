package com.github.vitaly1983g.restaurants.repository;

import com.github.vitaly1983g.restaurants.error.DataConflictException;
import org.springframework.data.jpa.repository.EntityGraph;
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

    @EntityGraph(attributePaths = {"dishes"}, type = EntityGraph.EntityGraphType.LOAD)
    // @Query("SELECT m FROM Menu m JOIN FETCH m.restaurant JOIN FETCH m.dishes WHERE m.id = :id and m.restaurant.id = :restId")
    @Query("SELECT m FROM Menu m WHERE m.id = :id and m.restaurant.id = :restId")
    Optional<Menu> getWithDish(int id, int restId);

    @Query("SELECT m FROM Menu m WHERE m.restaurant.id = :restId and m.menuDate = :menuDate")
    Optional<Menu> getByDate(LocalDate menuDate, int restId);

    @Query("SELECT m from Menu m WHERE m.restaurant.id=:restId AND m.menuDate >= :startDate AND m.menuDate < :endDate ORDER BY m.menuDate DESC")
    List<Menu> getBetweenHalfOpen(LocalDateTime startDate, LocalDateTime endDate, int restId);

    default Menu checkBelong(int id, int restId) {
        return get(id, restId).orElseThrow(
                () -> new DataConflictException("Menu with id=" + id + " doesn't belong to Restaurant id=" + restId));
    }
}