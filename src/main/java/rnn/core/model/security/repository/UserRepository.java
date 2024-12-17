package rnn.core.model.security.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rnn.core.model.admin.dto.UserCourseGroupDTO;
import rnn.core.model.security.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @EntityGraph(attributePaths = {"role"})
    @Query("""
        FROM User u LEFT JOIN FETCH u.userCourses uc WHERE uc.course.id != :courseId or uc.course.id is null
    """)
    List<User> findAllWithoutCourse(long courseId);

    @Query("""
        SELECT new rnn.core.model.admin.dto.UserCourseGroupDTO(
            u.username, u.email, r.name, g.name, uc.currentScore
        )
        FROM User u
        JOIN u.role r
        JOIN u.userCourses uc
        JOIN u.groups g
        WHERE uc.course.id = :courseId AND g.course.id = :courseId
    """)
    List<UserCourseGroupDTO> findAllWithUserCourseAndGroup(long courseId);
}
