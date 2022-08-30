package com.github.vitalyg1983.restaurants.web.menu;

import com.github.vitalyg1983.restaurants.model.Menu;
import com.github.vitalyg1983.restaurants.repository.MenuRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static com.github.vitalyg1983.restaurants.util.DateTimeUtil.NOW_DATE;

@Slf4j
public abstract class AbstractMenuController {

    @Autowired
    protected MenuRepository menuRepository;

    @Cacheable("menu")
    public ResponseEntity<Menu> getByDate(int restId, LocalDate menuDate) {
        log.info("get menu on date {} of restaurant {}", menuDate, restId);
        return ResponseEntity.of(menuRepository.getByDate(menuDate == null ? NOW_DATE : menuDate, restId));
    }

    @Cacheable("menus")
    public List<Menu> getAllForRestaurantsByDate(LocalDate menuDate) {
        log.info("get menu on date {} for all restaurants", menuDate);
        return menuRepository.getAllForRestaurantsByDate(menuDate == null ? NOW_DATE : menuDate);
    }
}