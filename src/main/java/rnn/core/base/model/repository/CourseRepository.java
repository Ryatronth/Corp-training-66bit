package rnn.core.base.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rnn.core.base.model.Course;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    boolean existsByTitle(String title);

    Optional<Course> findByTitle(String title);
}
