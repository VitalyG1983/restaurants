package com.github.vitaly1983g.restaurants.to;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class MenuTo extends BaseTo {

/*    @NotNull
    @DateTimeFormat(pattern = MenuUtil.DATE_PATTERN)
    private LocalDate menuDate;

    @NotNull
    private int restId;*/

    @Size(min = 1)
    @NotNull
    private Set<Integer> dishIds;

    public MenuTo() {
    }

    public MenuTo(Integer id, Set<Integer> dishIds) {
        super(id);
   /*     this.menuDate = menuDate;
        this.restId = restId;*/
        this.dishIds = dishIds;
    }
}
