package com.github.vitaly1983g.restaurants.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "menu", uniqueConstraints = {@UniqueConstraint(columnNames = {"menu_date", "rest_id"}, name = "menu_unique_date_restId_idx")})
//@Table(name = "menu", uniqueConstraints = {@UniqueConstraint(columnNames = {"menu_date", "rest_id"}, name = "menu_unique_date_restId_dishId_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@ToString(callSuper = true, exclude = {"restaurant","dishes"})
@ToString(callSuper = true, exclude = {"dishes"})
public class Menu extends BaseEntity {

    @Column(name = "menu_date", nullable = false)
    @NotNull
    private LocalDate menuDate;

/*    @Column(name = "rest_id", nullable = false)
    //@Schema(hidden = true)
    private int restId;*/

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rest_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) //https://stackoverflow.com/a/44988100/548473
    // @JsonManagedReference
    @Schema(hidden = true)
    private Restaurant restaurant;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "menu_id", nullable = false)
    @OrderBy("dish")
    @OnDelete(action = OnDeleteAction.CASCADE)
    //@JsonManagedReference
    private List<DishInMenu> dishesInMenu;


 /*   @Column(name = "rest_id", nullable = false, insertable = false, updatable = false)
    //@Schema(hidden = true)
    private int restId;

    @Column(name = "dish_id", nullable = false)
    private int dishId;*/

/*    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rest_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) //https://stackoverflow.com/a/44988100/548473
    //@Schema(hidden = true)
    private Restaurant restaurants;*/

/*    @OneToMany(fetch = FetchType.LAZY)//, mappedBy = "menu")
    @JoinColumn(name = "dish_id", nullable = false)
    @OrderBy("name")
    @OnDelete(action = OnDeleteAction.CASCADE)
    //@JsonManagedReference
    private List<Dish> dishIds;*/

    public Menu(Integer id, LocalDate menuDate) {
        super(id);
        this.menuDate = menuDate;
    }

    public Menu(Integer id, LocalDate menuDate, Restaurant restaurant) {
        super(id);
        this.menuDate = menuDate;
        this.restaurant = restaurant;
    }

    public Menu(Integer id, LocalDate menuDate, Restaurant restaurant, List<DishInMenu> dishesInMenu) {
        //super(id);
        this.id = id;
        this.menuDate = menuDate;
        this.restaurant = restaurant;
        this.dishesInMenu = dishesInMenu;
    }

 /*   public void setDishesInMenu(List<DishInMenu> dishesInMenu) {
        this.dishesInMenu.clear();
        if (dishesInMenu.size() != 0) {
            this.dishesInMenu.addAll(dishesInMenu);
        }
    }*/

    /* public LocalDate getMenuDate() {
        return menuDate;
    }*/

}
