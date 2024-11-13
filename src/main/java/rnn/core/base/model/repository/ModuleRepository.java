package rnn.core.base.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rnn.core.base.model.Module;

import java.util.List;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
    @Query("FROM Module m WHERE m.course.id = :courseId AND m.position > :position ORDER BY m.position DESC")
    List<Module> findAllWhichPositionIsHigher(Long courseId, int position);

    @Query("FROM Module m WHERE m.course.id = :courseId AND m.position >= :position ORDER BY m.position DESC")
    List<Module> findAllWhichPositionIsHigherOrEqual(Long courseId, int position);

    @Query(value = "FROM Module m LEFT JOIN FETCH m.topics WHERE m.course.id = :courseId ORDER BY m.position ASC")
    List<Module> findAllByCourseIdFetchTopic(Long courseId);
}
