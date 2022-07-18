package com.github.vitaly1983g.restaurants.to;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
public class LunchTo extends BaseTo {

    LocalDate date;

    String description;

    int calories;

    boolean excess;

    public LunchTo(Integer id, LocalDate date, String description, int calories, boolean excess) {
        super(id);
        this.date = date;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
    }
}
