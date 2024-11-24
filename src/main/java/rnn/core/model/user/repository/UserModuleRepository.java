package rnn.core.model.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rnn.core.model.user.UserModule;

@Repository
public interface UserModuleRepository extends JpaRepository<UserModule, Long> {
}
