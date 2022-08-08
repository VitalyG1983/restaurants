package com.github.vitaly1983g.restaurants.web.menu;

import com.github.vitaly1983g.restaurants.model.Menu;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = ProfileMenuRestaurantController.API_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class ProfileMenuRestaurantController extends AbstractMenuController {
    static final String API_URL = "/api/restaurants";

    @GetMapping("/{restId}/menus/by-date")
    public ResponseEntity<Menu> getByDate(@PathVariable int restId,
                                          @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
        return super.getByDate(restId, menuDate);
    }

    @GetMapping(name = "/menus/all-by-date")
    public List<Menu> allRestaurantsGetByDate(
            @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
        return super.allRestaurantsGetByDate(menuDate);
    }
}