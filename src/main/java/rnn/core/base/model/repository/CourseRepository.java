package rnn.core.base.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rnn.core.base.model.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
}
