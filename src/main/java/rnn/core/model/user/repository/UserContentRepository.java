package rnn.core.model.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rnn.core.model.user.UserContent;

import java.util.List;

@Repository
public interface UserContentRepository extends JpaRepository<UserContent, Long> {
    @Query("""
        FROM UserContent uc
        JOIN FETCH uc.content c
        WHERE uc.id IN (
            SELECT MAX(uc1.id)
            FROM UserContent uc1
            WHERE uc1.topic.id = :topicId
            GROUP BY uc1.content.id
        )
        ORDER BY uc.id ASC
    """)
    List<UserContent> findAllByTopicIdFetchContent(long topicId);
}
