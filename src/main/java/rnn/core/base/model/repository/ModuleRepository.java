package rnn.core.base.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rnn.core.base.model.Module;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
    Optional<Module> findByTitleAndCourseId(String name, Long courseId);

    List<Module> findByCourseId(Long courseId);

    @Query("FROM Module m WHERE m.course.id = :courseId AND m.position > :position")
    List<Module> findAllWhichPositionIsHigher(Long courseId, int position);
}
