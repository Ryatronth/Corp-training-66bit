package rnn.core.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rnn.core.security.authentication.UserInfo;
import rnn.core.security.model.Role;
import rnn.core.security.model.User;
import rnn.core.security.model.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;

    @Transactional
    public User checkUserExisted(UserInfo info) {
        try {
            return getUser(info.username());
        } catch (IllegalArgumentException ignored) {
            return createNewUser(info);
        }
    }

    public User createNewUser(UserInfo info) {
        Role userRole = roleService.getUserRole();
        User newUser = User
                .builder()
                .username(info.username())
                .email(info.email())
                .avatarUrl(info.avatarUrl())
                .role(userRole)
                .build();

        userRepository.save(newUser);
        return newUser;
    }

    public User getUser(String username) {
        return userRepository.findById(username).orElseThrow(() -> new IllegalArgumentException("Пользователь с именем \"%s\" не существует.".formatted(username)));
    }
}
