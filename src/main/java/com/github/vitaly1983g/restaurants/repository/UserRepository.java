package com.github.vitaly1983g.restaurants.repository;

import com.github.vitaly1983g.restaurants.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface UserRepository extends BaseRepository<User> {

    @Query("SELECT u FROM User u WHERE u.email = LOWER(:email)")
    Optional<User> findByEmailIgnoreCase(String email);

    //    https://stackoverflow.com/a/46013654/548473
    @EntityGraph(attributePaths = {"votes"}, type = EntityGraph.EntityGraphType.LOAD)
   // @Query("SELECT u FROM User u WHERE u.id=?1 AND u.votes>=?2 AND u.vote.dtVote<=?3")
    @Query("SELECT u FROM User u WHERE u.id=?1")
    // @Query("SELECT u FROM User u WHERE u.id=?1")
    Optional<User> getWithVotes(int id);
   // Optional<User> getWithCurrentVote(int id, LocalDateTime now, LocalDateTime end);

    @EntityGraph(attributePaths = {"votes"}, type = EntityGraph.EntityGraphType.LOAD)
    // @Query("SELECT u FROM User u WHERE u.id=?1 AND u.votes>=?2 AND u.vote.dtVote<=?3")
    @Query("SELECT u FROM User u WHERE u.id=?1")
        // @Query("SELECT u FROM User u WHERE u.id=?1")
    Optional<User> getWithCurrentVote(int id);
}