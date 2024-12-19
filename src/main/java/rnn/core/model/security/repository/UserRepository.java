package rnn.core.model.security.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rnn.core.model.admin.dto.UserCourseGroupDTO;
import rnn.core.model.admin.dto.UserGroupDTO;
import rnn.core.model.security.User;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @EntityGraph(attributePaths = {"role"})
    @Query("""
        FROM User u
        WHERE u.username in :usernames
    """)
    Set<User> findAllByUsernames(List<String> usernames);

    @EntityGraph(attributePaths = {"role"})
    @Query("""
        FROM User u
        LEFT JOIN FETCH u.userCourses uc
        WHERE uc.course.id <> :courseId OR uc.course.id IS NULL
    """)
    Page<User> findAllWithoutCourse(long courseId, Pageable pageable);

    @EntityGraph(attributePaths = {"role"})
    @Query("""
        FROM User u
        LEFT JOIN FETCH u.userCourses uc
        WHERE uc.user.username NOT IN (
            SELECT uc.user.username
            FROM UserCourse uc
            WHERE uc.course.id = :courseId
        )
        OR uc.course.id IS NULL
        OR u.username IN (
            SELECT uc1.user.username
            FROM u.userCourses uc1
            LEFT JOIN u.groups g ON uc1.course.id = g.course.id
            WHERE uc1.course.id = :courseId AND g.isDefault = true
        )
    """)
    Page<User> findAllWithoutCourseOrInDefault(long courseId, Pageable pageable);

    @EntityGraph(attributePaths = {"role"})
    @Query("""
        SELECT new rnn.core.model.admin.dto.UserGroupDTO(
            u,
            CASE WHEN :groupId IN (SELECT g.id FROM u.groups g WHERE g.id = :groupId) THEN true ELSE false END
        )
        FROM User u
        LEFT JOIN u.userCourses uc ON uc.course.id = :courseId
        WHERE uc.course.id <> :courseId
        OR uc.id IS NULL
        OR :groupId IN (SELECT g.id FROM u.groups g WHERE g.id = :groupId)
        OR u.username IN (
            SELECT uc1.user.username
            FROM u.userCourses uc1
            LEFT JOIN u.groups g ON uc1.course.id = g.course.id
            WHERE uc1.course.id = :courseId AND g.isDefault = true
        )
    """)
    Page<UserGroupDTO> findAllWithoutCourseOrInGroupOrInDefault(long courseId, long groupId, Pageable pageable);

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
    Page<UserCourseGroupDTO> findAllWithCourseAndGroup(long courseId, Pageable pageable);
}
