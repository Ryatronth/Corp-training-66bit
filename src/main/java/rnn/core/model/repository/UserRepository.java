package rnn.core.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rnn.core.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
