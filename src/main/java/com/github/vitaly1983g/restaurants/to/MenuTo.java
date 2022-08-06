package com.github.vitaly1983g.restaurants.to;

import com.github.vitaly1983g.restaurants.model.Restaurant;
import com.github.vitaly1983g.restaurants.util.MenuUtil;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

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
    private int restId;

    @Size(min = 1)
    private Set<Integer> dishIds;

    public MenuTo() {
    }

    public MenuTo(int id, LocalDate menuDate, int restId, Set<Integer> dishIds) {
        this.id = id;
        this.menuDate = menuDate;
        this.restId = restId;
        this.dishIds = dishIds;
    }
}
