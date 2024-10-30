package rnn.core.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rnn.core.security.model.Role;
import rnn.core.security.model.repository.RoleRepository;

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
