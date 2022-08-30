package com.github.vitalyg1983.restaurants.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

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
    @PositiveOrZero
    private int price;

    @Column(name = "rest_id", nullable = false)
    //@Range(min = 1)
    // not need in request/response, because restId get from url
    @JsonIgnore
    private int restId;

    public Dish(Integer id, String name, int price, int restId) {
        super(id, name);
        this.price = price;
        this.restId = restId;
    }
}
