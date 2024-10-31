package rnn.core.base.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rnn.core.base.model.Content;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
}
