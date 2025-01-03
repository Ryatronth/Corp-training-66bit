package rnn.core.model.user.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rnn.core.model.user.UserContent;

import java.util.Optional;

@Repository
public interface UserContentRepository extends JpaRepository<UserContent, Long> {
    @EntityGraph(attributePaths = {"content.answers"})
    @Query("""
        FROM UserContent uc
        JOIN uc.content c
        JOIN c.answers a
    """)
    Optional<UserContent> findByIdWithContentAndAnswers(long id);
}
