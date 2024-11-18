package rnn.core.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rnn.core.model.security.Role;
import rnn.core.model.security.repository.RoleRepository;

@RequiredArgsConstructor
@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public Role getUserRole() {
        return roleRepository.getReferenceById(Role.Name.USER);
    }

    public Role getAdminRole() {
        return roleRepository.getReferenceById(Role.Name.ADMIN);
    }
}
