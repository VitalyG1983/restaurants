package com.github.vitaly1983g.restaurants.to;

import com.github.vitaly1983g.restaurants.model.Dish;
import com.github.vitaly1983g.restaurants.model.Restaurant;
import com.github.vitaly1983g.restaurants.util.MenuUtil;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class MenuTo extends BaseTo {

    @NotNull
    @DateTimeFormat(pattern = MenuUtil.DATE_PATTERN)
    private LocalDate menuDate;

    @NotNull
    private Restaurant restaurant;

    @Size(min = 1)
    private List<Dish> dishes;

    public MenuTo() {
    }

    public MenuTo(int id, LocalDate menuDate, Restaurant restaurant, List<Dish> dishes) {
        this.id = id;
        this.menuDate = menuDate;
        this.restaurant = restaurant;
        this.dishes = dishes;
    }
}
