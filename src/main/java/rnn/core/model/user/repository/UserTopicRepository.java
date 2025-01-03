package rnn.core.model.user.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rnn.core.model.user.UserTopic;

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
}
