package rnn.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rnn.core.model.Role;
import rnn.core.model.User;
import rnn.core.model.repository.UserRepository;
import rnn.core.security.authentication.UserInfo;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;

    @Transactional
    public User checkUserExisted(UserInfo info) {
        Optional<User> user = userRepository.findById(info.username());

        if (user.isPresent()) {
            return user.get();
        }

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
        return userRepository.findById(username).orElse(null);
    }
}
