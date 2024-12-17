package rnn.core.model.security.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rnn.core.model.security.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @EntityGraph(attributePaths = {"role"})
    @Query("""
        FROM User u JOIN FETCH u.userCourses uc WHERE uc.course.id != :courseId
    """)
    List<User> findAllWithoutCourse(long courseId);

    @EntityGraph(attributePaths = {"role"})
    @Query("""
        FROM User u JOIN FETCH u.userCourses uc WHERE uc.course.id = :courseId
    """)
    List<User> findAllWithCourse(long courseId);
}
