package com.github.vitalyg1983.restaurants.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
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
@ToString
public class Vote extends BaseEntity {

    @Column(name = "time_vote", nullable = false)
    @NotNull
    private LocalTime timeVote;

    @Column(name = "date_vote", nullable = false)
    @NotNull
    private LocalDate dateVote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private User user;

    @Column(name = "rest_id", nullable = false)
    @Range(min = 1)
    private int restId;

    public Vote(Integer id, LocalDateTime dtVote, User user, int restId) {
        super(id);
        this.timeVote = dtVote.toLocalTime();
        this.dateVote = dtVote.toLocalDate();
        this.user = user;
        this.restId = restId;
    }
}
