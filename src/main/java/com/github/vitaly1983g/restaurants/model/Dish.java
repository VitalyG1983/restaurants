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
@ToString
public class Dish extends NamedEntity {

    @Column(name = "price", nullable = false)
    @NotNull
    @Range(min = 0)
    private Integer price;

    @Column(name = "rest_id", nullable = false)
    @Range(min = 1)
    private int restId;

    public Dish(Integer id, String name, int price, int restId) {
        super(id, name);
        this.price = price;
        this.restId = restId;
    }
}
