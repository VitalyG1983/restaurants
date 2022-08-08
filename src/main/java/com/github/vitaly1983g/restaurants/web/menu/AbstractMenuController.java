package com.github.vitaly1983g.restaurants.web.menu;

import com.github.vitaly1983g.restaurants.model.Menu;
import com.github.vitaly1983g.restaurants.repository.MenuRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

@Slf4j
public abstract class AbstractMenuController {

    @Autowired
    protected MenuRepository menuRepository;

    public ResponseEntity<Menu> getByDate(int restId, LocalDate menuDate) {
        log.info("get menu on date {} of restaurant {}", menuDate, restId);
        return  ResponseEntity.of(menuRepository.getByDate(menuDate, restId));
       /* if (menus.size() == 0) {      ?????
            throw new EntityNotFoundException("Not found menu on date=" + menuDate + " for restaurants with id=" + restId);
        }*/
       // return ResponseEntity.of(MenuUtil.getMenuTosByDateForRestaurants(menus, menuDate).stream().findFirst());
    }

    protected List<Menu> allRestaurantsGetByDate(LocalDate menuDate) {
        log.info("get menu on date {} for all restaurants", menuDate);
       // List<Menu> menus = menuRepository.getByDateAllRestaurants(menuDate);
       // return MenuUtil.getMenuTosByDateForRestaurants(menus, menuDate);
        return menuRepository.allRestaurantsGetByDate(menuDate);
    }

  /*  @CacheEvict(value = "users", allEntries = true)
    public void delete(int id) {
        log.info("delete {}", id);
        menuRepository.deleteExisted(id);
    }*/
}