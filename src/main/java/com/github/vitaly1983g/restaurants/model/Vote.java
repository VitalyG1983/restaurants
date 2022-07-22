package com.github.vitaly1983g.restaurants.model;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "vote", uniqueConstraints = {@UniqueConstraint(columnNames = {"date_vote", "user_id"}, name = "vote_unique_date_userId_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)//, exclude = {"restaurant"})
public class Vote extends BaseEntity {

    @Column(name = "dt_vote", nullable = false)
    @NotNull
    private LocalDateTime dtVote;

    @Column(name = "date_vote", nullable = false)
    @NotNull
    private LocalDate dateVote;

    //@Column(name = "dish_id", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rest_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) //https://stackoverflow.com/a/44988100/548473
    //@JsonBackReference
    //@Schema(hidden = true)
    private Restaurant restaurant;

    public Vote(Integer id, LocalDateTime dtVote, User user, Restaurant restaurant) {
        super(id);
        this.dtVote = dtVote;
        this.dateVote = dtVote.toLocalDate();
        this.user = user;
        this.restaurant = restaurant;
    }
}
