package rnn.core.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rnn.core.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Role.Name> {
}
