package rnn.core.model.admin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import rnn.core.model.admin.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>, QuerydslPredicateExecutor<Course> {
    @EntityGraph(attributePaths = {"author.role"})
    @Query("""
        FROM Course c
        WHERE c.id NOT in (
            SELECT uc.course.id
            FROM c.userCourses uc
            WHERE uc.user.username = :username
        )
        AND c.isPublished = true
    """)
    Page<Course> findCoursesNotEnrolledByUser(String username, Pageable pageable);
}
