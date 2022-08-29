package com.github.vitalyg1983.restaurants.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "dish_in_menu", uniqueConstraints = {@UniqueConstraint(columnNames = {"menu_id", "dish_id"}, name = "dish_in_menu_unique_menu_id_dish_id_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class DishInMenu extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    @ToString.Exclude
    public Dish dish;

    public DishInMenu(Dish dish) {
        this.dish = dish;
    }

    @Override
    @JsonIgnore  // https://stackoverflow.com/questions/17319266/can-i-annotate-a-member-inherited-from-a-superclass
    public void setId(Integer id) {
        super.setId(id);
    }
}
