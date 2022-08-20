package com.github.vitalyg1983.restaurants.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "dishinmenu", uniqueConstraints = {@UniqueConstraint(columnNames = {"menu_id", "dish_id"}, name = "dishinmenu_unique_menu_id_dish_id_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class DishInMenu extends BaseEntity {

    @JsonIgnore
    protected Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Dish dish;

    public DishInMenu(Dish dish) {
        this.dish = dish;
    }
}
