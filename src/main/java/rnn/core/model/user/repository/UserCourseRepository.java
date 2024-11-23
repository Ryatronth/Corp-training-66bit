package rnn.core.model.user.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rnn.core.model.user.UserCourse;

import java.util.List;

@Repository
public interface UserCourseRepository extends JpaRepository<UserCourse, Long> {
    @EntityGraph(attributePaths = {"group.course", "user.role"})
    List<UserCourse> findByUserUsername(String username);
}
