package rnn.core.model.admin.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rnn.core.model.admin.Topic;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    @Query("FROM Topic t WHERE t.module.id = :moduleId ORDER BY t.position ASC")
    List<Topic> findByModuleIdOrderByPositionAsc(long moduleId);

    @Query("FROM Topic t WHERE t.module.id = :moduleId AND t.position > :position ORDER BY t.position DESC")
    List<Topic> findAllWhichPositionIsHigher(long moduleId, int position);

    @Query("FROM Topic t WHERE t.module.id = :moduleId AND t.position >= :position ORDER BY t.position DESC")
    List<Topic> findAllWhichPositionIsHigherOrEqual(long moduleId, int position);

    @EntityGraph(attributePaths = {"module.course"})
    @Query("""
        FROM Topic t
        JOIN t.module m
        JOIN m.course c
        WHERE t.id = :id
    """)
    Optional<Topic> findByIdWithModuleAndCourse(long id);
}
