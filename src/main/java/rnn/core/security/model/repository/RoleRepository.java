package rnn.core.security.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rnn.core.security.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Role.Name> {
}
