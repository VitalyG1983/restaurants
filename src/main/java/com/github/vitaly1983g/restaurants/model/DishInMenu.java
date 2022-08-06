package com.github.vitaly1983g.restaurants.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.vitaly1983g.restaurants.HasId;
import lombok.*;
import org.springframework.data.domain.Persistable;
import org.springframework.data.util.ProxyUtils;
import org.springframework.util.Assert;

import javax.persistence.*;

@Entity
@Table(name = "dishinmenu", uniqueConstraints = {@UniqueConstraint(columnNames = {"menu_id", "dish_id"}, name = "dishinmenu_unique_menu_id_dish_id_idx")})
@Access(AccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@AllArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
//public class DishInMenu extends BaseEntity {
public class DishInMenu implements Persistable<Integer>, HasId {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Schema(accessMode = Schema.AccessMode.READ_ONLY) // https://stackoverflow.com/a/28025008/548473
    @JsonIgnore
    protected Integer id;

/*    @ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "menu_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) //https://stackoverflow.com/a/44988100/548473
    //@Schema(hidden = true)
    //@Range(min = 1)
    //@NotNull
    //@JsonBackReference
    @JsonIgnore
    private Menu menu;*/


/*    @Column(name = "menu_id")//, nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) //https://stackoverflow.com/a/44988100/548473
    //@Schema(hidden = true)
    //@Range(min = 1)
    //@NotNull
    //@JsonBackReference
    @JsonIgnore
    private Integer menuId;*/

   /* @Column(name = "dish_id", nullable = false)
    //@OnDelete(action = OnDeleteAction.CASCADE)
    public int dishId;*/

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id", nullable = false)
    public Dish dish;

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

    public DishInMenu(Integer id, Dish dish) {
        this.id = id;
       // this.menu = menu;
        this.dish = dish;
    }

    // doesn't work for hibernate lazy proxy
    public int id() {
        Assert.notNull(id, "Entity must have id");
        return id;
    }

    @JsonIgnore
    @Override
    public boolean isNew() {
        return id == null;
    }

    //    https://stackoverflow.com/questions/1638723
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !getClass().equals(ProxyUtils.getUserClass(o))) {
            return false;
        }
        BaseEntity that = (BaseEntity) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ":" + id;
    }
}
