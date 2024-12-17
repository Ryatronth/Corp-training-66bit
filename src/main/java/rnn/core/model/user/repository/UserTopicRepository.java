package rnn.core.model.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rnn.core.model.user.UserTopic;

@Repository
public interface UserTopicRepository extends JpaRepository<UserTopic, Long> {
}
