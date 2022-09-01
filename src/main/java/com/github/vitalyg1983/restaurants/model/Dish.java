package com.github.vitalyg1983.restaurants.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "dish", uniqueConstraints = {
        @UniqueConstraint(name = "dish_unique_rest_id_name_idx", columnNames = {"rest_id", "name"})
})
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Dish extends NamedEntity {

    @Column(name = "price", nullable = false)
    @NotNull
    @PositiveOrZero
    private int price;

    @Column(name = "rest_id", nullable = false)
    @Range(min = 1)
    private int restId;

    // this field 'menus' used only for @OnDelete in 'dish_in_menu' rows when delete Dish or Restaurant
/*    @ElementCollection
    @CollectionTable(name = "dish_in_menu", joinColumns = @JoinColumn(name = "dish_id"))
    @JoinColumn(name = "dish_id")
    @Column(name = "menu_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    @ToString.Exclude
    private Set<Integer> menus;*/

   /* @ManyToMany(mappedBy = "dishesInMenu")
    @OnDelete(action = OnDeleteAction.CASCADE)
    Set<Menu> menus;*/

    public Dish(Dish dish) {
        this(dish.getId(), dish.getName(), dish.getPrice(), dish.getRestId());
    }

    public Dish(Integer id, String name, int price, int restId) {
        super(id, name);
        this.price = price;
        this.restId = restId;
    }
}
