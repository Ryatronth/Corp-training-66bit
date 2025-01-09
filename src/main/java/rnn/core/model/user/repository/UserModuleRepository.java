package rnn.core.model.user.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rnn.core.model.user.UserModule;
import rnn.core.model.user.repository.projection.UserModuleCourseProjection;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserModuleRepository extends JpaRepository<UserModule, Long> {
    @EntityGraph(attributePaths = {"module", "topics"})
    @Query("""
        FROM UserModule um
        LEFT JOIN um.topics ut
        JOIN um.module m
        WHERE um.course.id = :userCourseId
        ORDER BY um.module.id ASC, ut.topic.id ASC
    """)
    List<UserModule> findByCourseIdFetchTopics(long userCourseId);

    @EntityGraph(attributePaths = {"module", "course.course"})
    @Query("""
        FROM UserModule um
        JOIN um.module m
        JOIN um.course uc
        JOIN uc.course c
        WHERE um.id = :id
    """)
    Optional<UserModule> findByIdFetchModuleUserModuleCourseUserCourse(long id);

    @Query("""
        SELECT um as userModule, uc as userCourse, c as course
        FROM UserModule um
        RIGHT JOIN um.course uc
        ON um.course.id = uc.id AND um.module.id = :moduleId
        LEFT JOIN uc.course c
    """)
    List<UserModuleCourseProjection> findAllByModuleIdFetchUserCourseAndCourse(long moduleId);
}
