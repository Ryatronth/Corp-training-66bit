package rnn.core.model.admin.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rnn.core.model.admin.Content;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    List<Content> findByTopicIdOrderByPositionAsc(long topicId);

    @Query("""
        SELECT DISTINCT c FROM Content c
        LEFT JOIN FETCH c.answers a
        WHERE c.topic.id = :topicId
        ORDER BY c.position ASC
    """)
    List<Content> findByTopicIdOrderByPositionAscFetchAnswers(long topicId);

    @Query("FROM Content c WHERE c.topic.id = :topicId AND c.position > :position ORDER BY c.position DESC")
    List<Content> findAllWhichPositionIsHigher(long topicId, int position);

    @Query("FROM Content c WHERE c.topic.id = :topicId AND c.position >= :position ORDER BY c.position DESC")
    List<Content> findAllWhichPositionIsHigherOrEqual(long topicId, int position);

    @Query("""
        FROM Content c
        LEFT JOIN FETCH c.answers a
        WHERE c.id = :id AND (a.isRight is true OR a.id is NULL)
        ORDER BY c.position ASC
    """)
    Optional<Content> findByIdOrderByPositionAscFetchAnswers(long id);

    @EntityGraph(attributePaths = {"topic"})
    @Query("""
        FROM Content co
        JOIN co.topic t
        LEFT JOIN FETCH co.answers a
        WHERE co.id = :id
        ORDER BY co.position ASC
    """)
    Optional<Content> findByIdFetchAnswersAndTopic(long id);
}
