package rnn.core.model.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rnn.core.model.admin.Topic;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    List<Topic> findByModuleIdOrderByPositionAsc(long moduleId);

    @Query("FROM Topic t WHERE t.module.id = :moduleId AND t.position > :position ORDER BY t.position DESC")
    List<Topic> findAllWhichPositionIsHigher(long moduleId, int position);

    @Query("FROM Topic t WHERE t.module.id = :moduleId AND t.position >= :position ORDER BY t.position DESC")
    List<Topic> findAllWhichPositionIsHigherOrEqual(long moduleId, int position);
}
