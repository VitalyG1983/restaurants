package com.github.vitaly1983g.restaurants.repository;

import com.github.vitaly1983g.restaurants.model.Restaurant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    @Query("SELECT r FROM Restaurant r WHERE r.id=:id ORDER BY r.name")
    List<Restaurant> getAll(int id);

   /* @Query("SELECT r from Restaurant r WHERE r.user.id=:userId AND r.dateTime >= :startDate AND r.dateTime < :endDate ORDER BY r.dateTime DESC")
    List<Restaurant> getBetweenHalfOpen(LocalDateTime startDate, LocalDateTime endDate, int userId);

    @Query("SELECT r FROM Restaurant r WHERE r.id = :id and r.user.id = :userId")
    Optional<Restaurant> get(int id, int userId);

    @Query("SELECT r FROM Restaurant r JOIN FETCH r.user WHERE r.id = :id and r.user.id = :userId")
    Optional<Restaurant> getWithUser(int id, int userId);*/

 /*   default Restaurant checkBelong(int id, int userId) {
        return get(id, userId).orElseThrow(
                () -> new DataConflictException("Restaurant id=" + id + " doesn't belong to User id=" + userId));
    }*/
}