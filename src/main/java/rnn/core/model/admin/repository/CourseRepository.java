package rnn.core.model.admin.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rnn.core.model.admin.Course;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    @EntityGraph(attributePaths = {"author.role"})
    @Query("""
        FROM Course c
        WHERE NOT EXISTS (
            SELECT uc FROM UserCourse uc
            WHERE uc.group.course = c
            AND uc.user.username = :username
        )
    """)
    List<Course> findCoursesNotEnrolledByUser(String username);
}
