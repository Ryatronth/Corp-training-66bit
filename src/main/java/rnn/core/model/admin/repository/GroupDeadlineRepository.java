package rnn.core.model.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rnn.core.model.admin.GroupDeadline;

import java.util.List;

@Repository
public interface GroupDeadlineRepository extends JpaRepository<GroupDeadline, Long> {
    @Query("""
        FROM GroupDeadline gd
        JOIN FETCH gd.module
        WHERE gd.group.id = :groupId
        ORDER BY gd.module.id ASC
    """)
    List<GroupDeadline> findAllByGroupIdFetchModule(long groupId);
}
