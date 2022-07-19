package com.github.vitaly1983g.restaurants.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.github.vitaly1983g.restaurants.util.validation.NoHtml;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "restaurant")//, uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id"}, name = "lunches_unique_userId_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)//, exclude = {"restaurant"})
public class Restaurant extends NamedEntity{

   // @Column(name = "menu_date")//, nullable = false)
 
 /*   @JoinColumn(name = "rest_id", nullable = false)
    @NotNull
    private LocalDate menuDate;*/

    @Column(name = "address", nullable = false)
    @NotBlank
    @Size(min = 2, max = 120)
    @NoHtml
    private String address;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")//, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OrderBy("createdDate, name")
    @JsonManagedReference
    @OnDelete(action = OnDeleteAction.CASCADE) //https://stackoverflow.com/a/44988100/548473
    @Schema(hidden = true)
    private List<Dish> menu;


}
