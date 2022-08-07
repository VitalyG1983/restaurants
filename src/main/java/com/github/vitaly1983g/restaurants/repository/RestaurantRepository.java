package com.github.vitaly1983g.restaurants.repository;

import com.github.vitaly1983g.restaurants.error.DataConflictException;
import com.github.vitaly1983g.restaurants.model.Restaurant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

/*    @Query("SELECT r FROM Restaurant r WHERE r.id=:id ORDER BY r.name")
    List<Restaurant> getAll(int id);*/

/*    @Query("SELECT r from Restaurant r WHERE r.user.id=:userId AND r.dateTime >= :startDate AND r.dateTime < :endDate ORDER BY r.dateTime DESC")
    List<Restaurant> getBetweenHalfOpen(LocalDateTime startDate, LocalDateTime endDate, int userId);*/

    @Query("SELECT r FROM Restaurant r WHERE r.id = :id")
    Optional<Restaurant> get(int id);

    @EntityGraph(attributePaths = {"dishes"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT r FROM Restaurant r WHERE r.id = :id")
    Optional<Restaurant> getWithDishes(int id);

  /*  @EntityGraph(attributePaths = {"menus"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT r FROM Restaurant r WHERE r.id = :id")
    Optional<Restaurant> getWithMenus(int id);*/

    default Restaurant checkExistence(int id) {
        return get(id).orElseThrow(
                () -> new DataConflictException("Restaurant id=" + id + " doesn't exist in database"));
    }
}