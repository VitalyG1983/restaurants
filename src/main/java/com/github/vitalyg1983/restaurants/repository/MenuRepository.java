package com.github.vitalyg1983.restaurants.repository;

import com.github.vitalyg1983.restaurants.error.DataConflictException;
import com.github.vitalyg1983.restaurants.model.Menu;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuRepository extends BaseRepository<Menu> {

    @EntityGraph(attributePaths = {"dishesInMenu", "restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Menu m WHERE m.restaurant.id=:restId ORDER BY m.menuDate")
    List<Menu> getAllForRestaurant(int restId);

   // @EntityGraph(attributePaths = {"dishesInMenu", "dishesInMenu.dish", "restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @EntityGraph(attributePaths = {"dishesInMenu", "restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Menu m WHERE m.id = :id and m.restaurant.id = :restId")
    Optional<Menu> get(int id, int restId);

    @EntityGraph(attributePaths = {"dishesInMenu", "restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Menu m WHERE m.restaurant.id = :restId AND m.menuDate = :menuDate")
    Optional<Menu> getByDate(LocalDate menuDate, int restId);

    @EntityGraph(attributePaths = {"dishesInMenu", "restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Menu m WHERE m.menuDate = :menuDate ORDER BY m.restaurant.id, m.menuDate")
    List<Menu> getAllForRestaurantsByDate(LocalDate menuDate);

    default Menu checkBelong(int id, int restId) {
        return get(id, restId).orElseThrow(
                () -> new DataConflictException("Menu id=" + id + " doesn't belong to Restaurant id=" + restId));
    }
}