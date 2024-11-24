package rnn.core.model.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rnn.core.model.admin.Module;

import java.util.List;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
    @Query("FROM Module m WHERE m.course.id = :courseId AND m.position > :position ORDER BY m.position DESC")
    List<Module> findAllWhichPositionIsHigher(Long courseId, int position);

    @Query("FROM Module m WHERE m.course.id = :courseId AND m.position >= :position ORDER BY m.position DESC")
    List<Module> findAllWhichPositionIsHigherOrEqual(Long courseId, int position);

    @Query("FROM Module m LEFT JOIN FETCH m.topics WHERE m.course.id = :courseId ORDER BY m.position ASC")
    List<Module> findAllByCourseIdFetchTopic(Long courseId);

    @Query(nativeQuery = true, value = """
                SELECT
                    m.id as id,
                    m.position as position,
                    m.title as title,
                    m.score as score,
                    d.id as d_id,
                    d.start_time as d_start_time,
                    d.end_time as d_end_time,
                    um.id as um_id,
                    um.current_score as um_current_score
                FROM public.module_t m
                LEFT JOIN public.deadline_t d ON m.id = d.module_id
                LEFT JOIN public.user_module_t um ON d.id = um.deadline_id
                WHERE m.course_id = :courseId
                AND (d.group_id = :groupId or d.group_id IS NULL)
                AND (um.course_id = :userCourseId or um.course_id IS NULL)
                ORDER BY m.position
            """)
    List<Object[]> findModulesWithDeadlinesAndUserModules(long courseId, long groupId, long userCourseId);
}
