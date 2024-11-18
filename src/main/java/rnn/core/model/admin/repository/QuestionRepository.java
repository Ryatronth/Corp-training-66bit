package rnn.core.model.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rnn.core.model.admin.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
}
