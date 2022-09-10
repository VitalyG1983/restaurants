package com.github.vitalydev.restaurants.model;

import com.fasterxml.jackson.annotation.JsonInclude;
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
/*    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "menu_id", nullable = false)
    @OrderBy("dish")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private List<DishInMenu> dishesInMenu;*/

    // Basic Many-to-Many through @JoinTable: https://www.baeldung.com/jpa-many-to-many
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "dish_in_menu",
            joinColumns = @JoinColumn(name = "menu_id", nullable = false, referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "dish_id", nullable = false, referencedColumnName = "id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"menu_id", "dish_id"}, name = "dish_in_menu_unique_menu_id_dish_id_idx")})
    @OrderBy("name")
    @ToString.Exclude
    //@JoinColumn(name = "dish_id")
    //@OnDelete(action = OnDeleteAction.CASCADE)
    private List<Dish> dishesInMenu;

    public Menu(Integer id, LocalDate menuDate, Restaurant restaurant) {
        super(id);
        this.menuDate = menuDate;
        this.restaurant = restaurant;
    }

    public Menu(Integer id, LocalDate menuDate, Restaurant restaurant, List<Dish> dishesInMenu) {
        this(id, menuDate, restaurant);
        this.dishesInMenu = dishesInMenu;
    }
}
