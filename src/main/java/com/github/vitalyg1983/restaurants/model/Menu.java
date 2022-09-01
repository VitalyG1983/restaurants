package com.github.vitalyg1983.restaurants.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "menu", uniqueConstraints = {@UniqueConstraint(columnNames = {"menu_date", "rest_id"}, name = "menu_unique_date_rest_id_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Menu extends BaseEntity {

    @Column(name = "menu_date", nullable = false)
    @NotNull
    private LocalDate menuDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rest_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) //https://stackoverflow.com/a/44988100/548473
    @NotNull
    //@JsonIgnore
    // 'restaurant' not need in Response when Admin retrieve menu data. Needed when User GET Menu of Restaurants
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @ToString.Exclude
    private Restaurant restaurant;

    // Many-to-Many With a New 3-d Entity: https://www.baeldung.com/jpa-many-to-many
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "menu_id", nullable = false)
    @OrderBy("dish")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private List<DishInMenu> dishesInMenu;

    public Menu(Integer id, LocalDate menuDate, Restaurant restaurant) {
        super(id);
        this.menuDate = menuDate;
        this.restaurant = restaurant;
    }

    public Menu(Integer id, LocalDate menuDate, Restaurant restaurant, List<DishInMenu> dishesInMenu) {
        this(id, menuDate, restaurant);
        this.dishesInMenu = dishesInMenu;
    }
}
