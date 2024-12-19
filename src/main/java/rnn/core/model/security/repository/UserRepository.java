package rnn.core.model.security.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rnn.core.model.admin.dto.UserCourseGroupDTO;
import rnn.core.model.admin.dto.UserGroupDTO;
import rnn.core.model.security.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @EntityGraph(attributePaths = {"role"})
    @Query("""
        FROM User u
        LEFT JOIN FETCH u.userCourses uc
        WHERE uc.user.username NOT IN (
            SELECT uc.user.username
            FROM UserCourse uc
            WHERE uc.course.id = :courseId
        ) OR uc.course.id IS NULL
    """)
    List<User> findAllWithoutCourse(long courseId);

    @EntityGraph(attributePaths = {"role"})
    @Query("""
        SELECT new rnn.core.model.admin.dto.UserGroupDTO(
            u,
            CASE WHEN :groupId IN (SELECT g.id FROM u.groups g WHERE g.id = :groupId) THEN true ELSE false END
        )
        FROM User u
        LEFT JOIN u.userCourses uc ON uc.course.id = :courseId
        WHERE uc.course.id <> :courseId OR uc.id IS NULL OR :groupId IN (SELECT g.id FROM u.groups g WHERE g.id = :groupId)
    """)
    List<UserGroupDTO> findAllWithoutCourseOrInGroup(long courseId, long groupId);

    @EntityGraph(attributePaths = {"role"})
    @Query("""
        SELECT new rnn.core.model.admin.dto.UserCourseGroupDTO(
            u, g, uc.currentScore
        )
        FROM User u
        JOIN u.role r
        JOIN u.userCourses uc
        JOIN u.groups g
        WHERE uc.course.id = :courseId AND g.course.id = :courseId
    """)
    List<UserCourseGroupDTO> findAllWithCourseAndGroup(long courseId);
}
