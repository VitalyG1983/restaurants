package com.github.vitaly1983g.restaurants.to;

import com.github.vitaly1983g.restaurants.model.Dish;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
//@FieldDefaults(makeFinal=true, level=AccessLevel.PRIVATE)
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class MenuTo extends BaseTo {

    LocalDate menuDate;

    @NonFinal
    int restId;

    List<Dish> dishes;

    public MenuTo() {
    }

    public MenuTo(int id, LocalDate menuDate, int restId, List<Dish> dishes) {
        this.id = id;
        this.menuDate = menuDate;
        this.restId = restId;
        this.dishes = dishes;
    }
}
