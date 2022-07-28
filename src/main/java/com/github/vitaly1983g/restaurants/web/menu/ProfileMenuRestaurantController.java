package com.github.vitaly1983g.restaurants.web.menu;

import com.github.vitaly1983g.restaurants.to.MenuTo;
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
@RequestMapping(value = ProfileMenuRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class ProfileMenuRestaurantController extends AbstractMenuController {
    static final String REST_URL = "/api/restaurants";

    @GetMapping("/{restId}/menus/{menuDate}")
    public ResponseEntity<MenuTo> getByDate(@PathVariable int restId,
                                            @PathVariable @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
        return super.getByDate(restId, menuDate);
    }

    @GetMapping(name = "/with-menu-on-date")
    public List<MenuTo> getByDateAllRestaurants(
            @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate menuDate) {
        return super.getByDateAllRestaurants(menuDate);
    }
}