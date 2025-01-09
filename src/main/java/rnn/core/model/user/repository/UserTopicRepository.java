package rnn.core.model.user.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rnn.core.model.user.UserTopic;
import rnn.core.model.user.repository.projection.UserTopicModuleCourseAndModuleCourseProjection;
import rnn.core.model.user.repository.projection.UserTopicModuleCourseProjection;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTopicRepository extends JpaRepository<UserTopic, Long> {
    @EntityGraph(attributePaths = {"module.course.course", "module.module", "topic"})
    @Query("""
        FROM UserTopic ut
        JOIN ut.topic t
        JOIN ut.module um
        JOIN um.module m
        JOIN um.course uc
        JOIN uc.course c
        WHERE ut.id = :id
    """)
    Optional<UserTopic> findByIdFetchModuleAndCourse(long id);

    @Query("""
        SELECT ut as userTopic, um as userModule, uc as userCourse
        FROM UserTopic ut
        RIGHT JOIN ut.module um
        ON ut.module.id = um.id AND ut.topic.id = :topicId
        RIGHT JOIN um.course uc
        ON um.course.id = uc.id AND um.module.id = :moduleId
    """)
    List<UserTopicModuleCourseProjection> findAllByTopicIdFetchUserModuleAndCourse(long topicId, long moduleId);

    @Query("""
        SELECT ut as userTopic, um as userModule, uc as userCourse, m as module, c as course
        FROM UserTopic ut
        RIGHT JOIN ut.module um
        RIGHT JOIN um.course uc
        ON um.course.id = uc.id AND ut.topic.id = :topicId
        LEFT JOIN um.module m
        LEFT JOIN uc.course c
    """)
    List<UserTopicModuleCourseAndModuleCourseProjection> findAllByTopicIdFetchModuleCourseUserModuleAndCourse(long topicId);
}
