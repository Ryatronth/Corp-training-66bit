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
        return roleRepository.findById(Role.Name.USER).orElseThrow(() -> new IllegalStateException("Роль 'Пользователя' не проинициализирована"));
    }

    public Role getAdminRole() {
        return roleRepository.findById(Role.Name.ADMIN).orElseThrow(() -> new IllegalStateException("Роль 'Админа' не проинициализирована"));
    }
}
