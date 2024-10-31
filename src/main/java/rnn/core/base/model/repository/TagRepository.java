package rnn.core.base.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rnn.core.base.model.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, String> {
}
