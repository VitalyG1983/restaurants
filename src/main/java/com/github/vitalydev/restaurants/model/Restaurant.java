package com.github.vitalydev.restaurants.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.vitalydev.restaurants.util.validation.NoHtml;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "restaurant", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "address"}, name = "rest_unique_name_address_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Restaurant extends NamedEntity {

    @Column(name = "address", nullable = false)
    @NotBlank
    @Size(min = 2, max = 120)
    @NoHtml
    private String address;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "rest_id")
    @OrderBy("name")
    @OnDelete(action = OnDeleteAction.CASCADE) //https://stackoverflow.com/a/44988100/548473
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Schema(hidden = true)
    @ToString.Exclude
    private List<Dish> dishes;

    public Restaurant(Integer id, String name, String address) {
        super(id, name);
        this.address = address;
    }
}
