package rnn.core.model.admin.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rnn.core.model.admin.GroupDeadline;

import java.util.Optional;

@Repository
public interface GroupDeadlineRepository extends JpaRepository<GroupDeadline, Long> {
    @EntityGraph(attributePaths = {"module"})
    Optional<GroupDeadline> findByModuleIdAndGroupId(long moduleId, long groupId);
}
