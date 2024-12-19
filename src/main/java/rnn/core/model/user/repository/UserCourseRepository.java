package rnn.core.model.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rnn.core.model.user.UserCourse;

@Repository
public interface UserCourseRepository extends JpaRepository<UserCourse, Long> {
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
}
