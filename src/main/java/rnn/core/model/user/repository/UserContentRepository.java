package rnn.core.model.user.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rnn.core.model.user.UserContent;
import rnn.core.model.user.repository.projection.UserContentTopicModuleCourseProjection;

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
        ORDER BY uc.content.id ASC
    """)
    List<UserContent> findAllByTopicIdFetchContent(long topicId);

    @EntityGraph(attributePaths = {"topic.module.course"})
    @Query("""
        FROM UserContent uc
        JOIN uc.topic ut
        JOIN ut.module um
        JOIN um.course uco
        WHERE uc.content.id = :contentId AND uc.isSuccess is true
    """)
    List<UserContent> findAllSuccessByContentIdFetchUserTopicModuleAndCourse(long contentId);

    @Query("""
        SELECT uc as userContent, ut as userTopic, um as userModule, uco as userCourse
        FROM UserContent uc
        RIGHT JOIN uc.topic ut
        RIGHT JOIN ut.module um
        RIGHT JOIN um.course uco
        on um.course.id = uco.id AND uc.content.id = :contentId
    """)
    List<UserContentTopicModuleCourseProjection> findAllByContentIdFetchUserTopicModuleAndCourse(long contentId);
}
