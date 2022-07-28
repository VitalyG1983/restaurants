package com.github.vitaly1983g.restaurants.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.github.vitaly1983g.restaurants.util.UserUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

@Entity
@Table(name = "menu", uniqueConstraints = {@UniqueConstraint(columnNames = {"menu_date", "rest_id", "dish_id"}, name = "menu_unique_date_restId_dishId_idx")})
//@Table(name = "menu", uniqueConstraints = {@UniqueConstraint(columnNames = {"menu_date", "rest_id"}, name = "menu_unique_date_restId_dishId_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)//, exclude = {"restaurant"})
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id", nullable = false)
    @OrderBy("name")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Dish dish;


 /*   @Column(name = "rest_id", nullable = false, insertable = false, updatable = false)
    //@Schema(hidden = true)
    private int restId;

    @Column(name = "dish_id", nullable = false)
    private int dishId;*/

/*    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rest_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) //https://stackoverflow.com/a/44988100/548473
    //@Schema(hidden = true)
    private Restaurant restaurant;*/

/*    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menu")
    @OrderBy("name")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference
    private List<Dish> dishIds;*/



    public Menu(Integer id, LocalDate menuDate) {
        super(id);
        this.menuDate = menuDate;
    }

    public Menu(Integer id, LocalDate menuDate, Restaurant restaurant, Dish dish ) {
        super(id);
        this.menuDate = menuDate;
        this.restaurant = restaurant;
        this.dish = dish;

    }

   /* public LocalDate getMenuDate() {
        return menuDate;
    }*/

}
