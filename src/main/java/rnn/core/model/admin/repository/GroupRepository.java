package rnn.core.model.admin.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rnn.core.model.admin.Group;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    @Query("FROM Group g WHERE g.course.id = :courseId ORDER BY g.name ASC")
    List<Group> findByCourseId(long courseId);

    @EntityGraph(attributePaths = {"userCourses.user.role"})
    @Query("""
        FROM Group g
        WHERE g.id = :groupId
    """)
    Optional<Group> findByIdWithUsers(long groupId);
}
