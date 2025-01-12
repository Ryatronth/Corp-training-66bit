package rnn.core.model.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rnn.core.model.user.UserCourse;
import rnn.core.model.user.dto.UserCourseWithCourseAndGroupDTO;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCourseRepository extends JpaRepository<UserCourse, Long> {
    @Query("""
        SELECT new rnn.core.model.user.dto.UserCourseWithCourseAndGroupDTO(c, uc, g)
        FROM UserCourse uc
        JOIN uc.course c
        JOIN c.groups g
        JOIN g.users u
        WHERE uc.id = :id AND u.id = :userId
    """)
    Optional<UserCourseWithCourseAndGroupDTO> findByIdFetchCourseAndGroup(long id, long userId);

    @Query("""
        FROM UserCourse uc
        WHERE uc.course.id = :courseId AND uc.user.id = :userId
    """)
    Optional<UserCourse> findByCourseIdAndUserId(long courseId, long userId);

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
        WHERE uc.course.id = :courseId AND uc.user.id IN :userIds
    """)
    void deleteAllByCourseIdAndUserIds(long courseId, List<Long> userIds);
}
