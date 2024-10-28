package rnn.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rnn.core.model.Role;
import rnn.core.model.repository.RoleRepository;

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
