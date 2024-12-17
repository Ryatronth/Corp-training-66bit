package rnn.core.model.user.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rnn.core.model.user.UserModule;

import java.util.List;

@Repository
public interface UserModuleRepository extends JpaRepository<UserModule, Long> {
    @EntityGraph(attributePaths = {"deadline", "topics"})
    List<UserModule> findByCourseId(long userCourseId);
}
