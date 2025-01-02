package rnn.core.model.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rnn.core.model.admin.Module;

import java.util.List;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
    @Query("FROM Module m WHERE m.course.id = :courseId AND m.position > :position ORDER BY m.position DESC")
    List<Module> findAllWhichPositionIsHigher(long courseId, int position);

    @Query("FROM Module m WHERE m.course.id = :courseId AND m.position >= :position ORDER BY m.position DESC")
    List<Module> findAllWhichPositionIsHigherOrEqual(long courseId, int position);

    @Query("FROM Module m WHERE m.course.id = :courseId")
    List<Module> findAllByCourseId(long courseId);

    @Query(" FROM Module m LEFT JOIN FETCH m.topics t WHERE m.course.id = :courseId ORDER BY m.position ASC, t.module.id ASC")
    List<Module> findAllByCourseIdFetchTopic(long courseId);
}
