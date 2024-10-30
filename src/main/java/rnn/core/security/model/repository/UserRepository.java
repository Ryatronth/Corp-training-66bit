package rnn.core.security.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rnn.core.security.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
