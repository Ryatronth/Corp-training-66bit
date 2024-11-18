package rnn.core.model.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rnn.core.model.security.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
