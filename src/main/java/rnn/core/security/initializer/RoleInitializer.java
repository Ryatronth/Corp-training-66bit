package rnn.core.security.initializer;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rnn.core.security.model.Role;
import rnn.core.security.model.repository.RoleRepository;

@RequiredArgsConstructor
@Service
public class RoleInitializer {
    private final RoleRepository roleRepository;

    @PostConstruct
    @Transactional
    public void initRoles() {
        for (Role.Name name : Role.Name.values()) {
            if (roleRepository.findById(name).isPresent()) {
                continue;
            }

            Role role = new Role();
            role.setName(name);
            roleRepository.save(role);
        }
    }
}
