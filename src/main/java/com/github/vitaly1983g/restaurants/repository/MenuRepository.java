package com.github.vitaly1983g.restaurants.repository;

import com.github.vitaly1983g.restaurants.error.DataConflictException;
import com.github.vitaly1983g.restaurants.model.Menu;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface MenuRepository extends BaseRepository<Menu> {

    @EntityGraph(attributePaths = {"dish"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Menu m WHERE m.restId=:restId ORDER BY m.menuDate")
    List<Menu> getAll(int restId);

 /*   @Query("SELECT m FROM Menu m WHERE m.id = :id and m.restId = :restId")
    Optional<Menu> get(int id, int restId);*/

/*    @EntityGraph(attributePaths = {"dishIds"}, type = EntityGraph.EntityGraphType.LOAD)
    // @Query("SELECT m FROM Menu m JOIN FETCH m.restaurant JOIN FETCH m.dishIds WHERE m.id = :id and m.restaurant.id = :restId")
    @Query("SELECT m FROM Menu m WHERE m.id = :id and m.restId = :restId")
    Optional<Menu> getWithDish(int id, int restId);*/

    @EntityGraph(attributePaths = {"dish"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Menu m WHERE m.restId = :restId and m.menuDate = :menuDate")
        //Optional<Menu> getByDate(LocalDate menuDate, int restId);
    List<Menu> getByDate(LocalDate menuDate, int restId);

   /* @Transactional
    @Modifying
    @Query("DELETE FROM Menu m WHERE m.menuDate=:menuDate AND m.restId = :restId")
    int deleteByDate(LocalDate menuDate, int restId);*/

    @Query("SELECT m from Menu m WHERE m.restId=:restId AND m.menuDate >= :startDate AND m.menuDate < :endDate ORDER BY m.menuDate DESC")
    List<Menu> getBetweenHalfOpen(LocalDateTime startDate, LocalDateTime endDate, int restId);

    default List<Menu> checkBelong(LocalDate menuDate, int restId) {
        List<Menu> menuByDate = getByDate(menuDate, restId);
        if (menuByDate.size() != 0) {
            return menuByDate;
        } else
            throw new DataConflictException("Menu on date=" + menuDate + " doesn't belong to Restaurant id=" + restId);
    }
}