package rnn.core.model.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rnn.core.model.user.UserCourse;
import rnn.core.model.user.dto.UserCourseDTO;

import java.util.List;

@Repository
public interface UserCourseRepository extends JpaRepository<UserCourse, Long> {
    @EntityGraph(attributePaths = {"course.author.role"})
    @Query("""
        SELECT new rnn.core.model.user.dto.UserCourseDTO(
            c, u
        )
        FROM UserCourse u
        JOIN u.course c
        WHERE u.user.username = :username
    """)
    Page<UserCourseDTO> findAllByUsernameWithCourse(String username, Pageable pageable);

    @Modifying
    @Query("""
        DELETE FROM UserCourse uc
        WHERE uc.course.id = :courseId AND uc.user.username IN (
            SELECT u.username
            FROM Group g
            JOIN g.users u
            WHERE g.id = :groupId
        )
    """)
    void deleteAllByCourseIdAndGroupId(long courseId, long groupId);

    @Modifying
    @Query("""
        DELETE FROM UserCourse uc
        WHERE uc.course.id = :courseId AND uc.user.username IN :usernames
    """)
    void deleteAllByCourseIdAndUsernames(long courseId, List<String> usernames);
}
