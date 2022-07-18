package com.github.vitaly1983g.restaurants.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "dish")
//, uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id"}, name = "lunches_unique_userId_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)//, exclude = {"restaurant"})
public class Dish extends NamedEntity {

    @Column(name = "created_date", nullable = false)
    @NotNull
    private LocalDate createdDate;

    @Column(name = "price", nullable = false)
    @NotNull
    @Range(min = 10, max = 5000)
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    @Schema(hidden = true)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "rest_id", nullable = false)
    @JsonBackReference
    @Schema(hidden = true)
    private Restaurant restaurant;

    public Dish(Integer id, LocalDate createdDate, String name, int price) {
        super(id, name);
        this.createdDate = createdDate;
        this.price = price;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }
}
