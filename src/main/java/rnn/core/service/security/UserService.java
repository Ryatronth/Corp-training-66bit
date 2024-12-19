package rnn.core.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rnn.core.authentication.UserInfo;
import rnn.core.model.security.Role;
import rnn.core.model.security.User;
import rnn.core.model.security.repository.UserRepository;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;

    @Transactional
    public User checkUserExisted(UserInfo info) {
        try {
            return findOne(info.username());
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

    public User findOne(String username) {
        return userRepository.findById(username).orElseThrow(() -> new IllegalArgumentException("Пользователь с именем \"%s\" не существует.".formatted(username)));
    }

    public Set<User> findAllByUsernames(List<String> usernames) {
        return userRepository.findAllByUsernames(usernames);
    }
}
