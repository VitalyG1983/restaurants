package com.github.vitaly1983g.restaurants.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "dish", uniqueConstraints = {
        @UniqueConstraint(name = "dish_unique_rest_id_name_idx", columnNames = {"rest_id", "name"})
})
@Getter
@Setter
@NoArgsConstructor
@ToString//, exclude = {"restaurants"})
public class Dish extends NamedEntity {

    @Column(name = "price", nullable = false)
    @NotNull
    @Range(min = 0)
    private Integer price;

    @Column(name = "rest_id")
    @OnDelete(action = OnDeleteAction.CASCADE) //https://stackoverflow.com/a/44988100/548473
    //@Schema(hidden = true)
    @Range(min = 1)
    @NotNull
    private int restId;

 /*   @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rest_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Restaurant restaurants;*/

    /* @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rest_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) //https://stackoverflow.com/a/44988100/548473
    //@Schema(hidden = true)
    private Restaurant restaurants;*/

  /*  @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private Menu menu;*/

    public Dish(Integer id, String name, int price, int restId) {
        super(id, name);
        this.price = price;
        this.restId = restId;
    }
}
