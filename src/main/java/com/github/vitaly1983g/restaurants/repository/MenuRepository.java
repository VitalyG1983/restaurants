package com.github.vitaly1983g.restaurants.repository;

import com.github.vitaly1983g.restaurants.error.DataConflictException;
import com.github.vitaly1983g.restaurants.model.DishInMenu;
import com.github.vitaly1983g.restaurants.model.Menu;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuRepository extends BaseRepository<Menu> {


/*    @Modifying
    @Transactional
    @Query("UPDATE Menu m SET m.id = :newId WHERE m.id =:id AND m.restaurant.id=:restId")
    int updateId(int id, int newId, int restId);*/

    @EntityGraph(attributePaths = {"dishesInMenu", "dishesInMenu.dish", "restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Menu m WHERE m.restaurant.id=:restId ORDER BY m.menuDate")
    List<Menu> getAllForRestaurant(int restId);

    @Query("SELECT m FROM Menu m WHERE m.id = :id and m.restaurant.id = :restId")
    Optional<Menu> get(int id, int restId);

    /*@EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Menu m WHERE m.id = :id")
    Menu getWithRestaurant(int id);*/

    @EntityGraph(attributePaths = {"dishesInMenu", "dishesInMenu.dish", "restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Menu m WHERE m.restaurant.id = :restId AND m.menuDate = :menuDate")
   // Optional<Menu> getByDate(LocalDate menuDate, int restId);
    Optional<Menu> getByDate(LocalDate menuDate, int restId);

    @EntityGraph(attributePaths = {"dishesInMenu", "dishesInMenu.dish", "restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Menu m WHERE m.menuDate = :menuDate ORDER BY m.restaurant.id, m.menuDate")
    List<Menu> getAllForRestaurantsByDate(LocalDate menuDate);

    @Query("SELECT m from Menu m WHERE m.restaurant.id=:restId AND m.menuDate >= :startDate AND m.menuDate < :endDate ORDER BY m.menuDate DESC")
    List<Menu> getBetweenHalfOpen(LocalDateTime startDate, LocalDateTime endDate, int restId);

    default Menu checkBelong(int id, int restId) {
        return get(id, restId).orElseThrow(
                () -> new DataConflictException("Menu id=" + id + " doesn't belong to Restaurant id=" + restId));
    }
}