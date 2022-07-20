package com.github.vitaly1983g.restaurants.model;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "menu", uniqueConstraints = {@UniqueConstraint(columnNames = {"menu_date", "rest_id", "dish_id"}, name = "menu_unique_date_restId_dishId_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)//, exclude = {"restaurant"})
public class Menu extends BaseEntity {

    @Column(name = "menu_date", nullable = false)
    @NotNull
    private LocalDate menuDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rest_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) //https://stackoverflow.com/a/44988100/548473
    //@JsonBackReference
    //@Schema(hidden = true)
    private Restaurant restaurant;

    //@Column(name = "dish_id", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id", nullable = false)
    private Dish dish;

    public Menu(Integer id, LocalDate menuDate) {
        super(id);
        this.menuDate = menuDate;
    }

    public LocalDate getMenuDate() {
        return menuDate;
    }
}
