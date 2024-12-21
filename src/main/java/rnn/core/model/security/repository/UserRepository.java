package rnn.core.model.security.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rnn.core.model.security.User;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @EntityGraph(attributePaths = {"role"})
    @Query("""
        FROM User u
        WHERE u.username in :usernames
    """)
    Set<User> findAllByUsernames(List<String> usernames);
}
