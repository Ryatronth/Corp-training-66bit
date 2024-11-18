package rnn.core.model.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rnn.core.model.security.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Role.Name> {
}
