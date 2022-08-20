package com.github.vitalyg1983.restaurants.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.vitalyg1983.restaurants.util.validation.NoHtml;
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
@ToString(callSuper = true, exclude = {"dishes", "votes"})
public class Restaurant extends NamedEntity {

    @Column(name = "address", nullable = false)
    @NotBlank
    @Size(min = 2, max = 120)
    @NoHtml
    private String address;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "rest_id")
    @OrderBy("restId, name")
    @OnDelete(action = OnDeleteAction.CASCADE) //https://stackoverflow.com/a/44988100/548473
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Schema(hidden = true)
    private List<Dish> dishes;

    @OneToMany(fetch = FetchType.LAZY)//, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "rest_id")
    @OrderBy("dateVote, restId, timeVote")
    @OnDelete(action = OnDeleteAction.CASCADE) //https://stackoverflow.com/a/44988100/548473
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Schema(hidden = true)
    private List<Vote> votes;

    public Restaurant(Integer id, String name, String address) {
        super(id, name);
        this.address = address;
    }
}