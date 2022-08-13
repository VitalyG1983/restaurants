package com.github.vitaly1983g.restaurants.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "vote", uniqueConstraints = {
        @UniqueConstraint(name = "vote_unique_date_userId_idx", columnNames = {"date_vote", "user_id"})
})
@Getter
@Setter
@NoArgsConstructor
@ToString//, exclude = {"user"})
public class Vote extends BaseEntity {

    @Column(name = "time_vote", nullable = false)
    @NotNull
    private LocalTime timeVote;

    @Column(name = "date_vote", nullable = false)
    @NotNull
    @FutureOrPresent
    private LocalDate dateVote;

 /*   //@Column(name = "dish_id", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;*/

  /*  @Column(name = "user_id", nullable = false)
    @NotNull
    private int userId;*/

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    //@JsonBackReference
    private User user;

 /*   @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rest_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) //https://stackoverflow.com/a/44988100/548473
    //@JsonBackReference
    //@Schema(hidden = true)
    private Restaurant restaurant;*/

    @Column(name = "rest_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) //https://stackoverflow.com/a/44988100/548473
    //@Schema(hidden = true)
    @NotNull
    private int restId;

    public Vote(Integer id, LocalDateTime dtVote, User user, int restId) {
        super(id);
        this.timeVote = dtVote.toLocalTime();
        this.dateVote = dtVote.toLocalDate();
        this.user = user;
        this.restId = restId;
    }
}
