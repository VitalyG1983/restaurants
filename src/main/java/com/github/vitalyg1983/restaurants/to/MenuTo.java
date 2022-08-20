package com.github.vitalyg1983.restaurants.to;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MenuTo extends BaseTo {

    @Size(min = 1)
    @NotNull
    private Set<Integer> dishIds;

    public MenuTo(Integer id, Set<Integer> dishIds) {
        super(id);
        this.dishIds = dishIds;
    }
}
