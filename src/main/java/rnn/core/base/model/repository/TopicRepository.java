package rnn.core.base.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rnn.core.base.model.Topic;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    @Query("FROM Topic t WHERE t.module.id = :moduleId ORDER BY t.position ASC")
    List<Topic> findByModuleId(Long moduleId);

    @Query("FROM Topic t WHERE t.module.id = :moduleId AND t.position > :position ORDER BY t.position DESC")
    List<Topic> findAllWhichPositionIsHigher(Long moduleId, int position);

    @Query("FROM Topic t WHERE t.module.id = :moduleId AND t.position >= :position ORDER BY t.position DESC")
    List<Topic> findAllWhichPositionIsHigherOrEqual(Long moduleId, int position);
}
