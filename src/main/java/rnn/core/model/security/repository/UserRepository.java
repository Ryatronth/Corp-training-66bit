package rnn.core.model.security.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rnn.core.model.security.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @EntityGraph(attributePaths = {"role"})
    @Query("""
        FROM User u
        WHERE u.id in :ids
    """)
    Set<User> findAllByIds(List<Long> ids);

    @EntityGraph(attributePaths = {"role"})
    @Query("""
        FROM User u
        WHERE u.email = :email
    """)
    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = {"role"})
    @Query("""
        FROM User u
        WHERE u.email = :email OR u.gitHubId = :providerId OR u.gitLabId = :providerId
    """)
    Optional<User> findByEmailOrProviderId(String email, String providerId);
}
