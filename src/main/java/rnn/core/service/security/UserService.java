package rnn.core.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rnn.core.authentication.UserInfo;
import rnn.core.authentication.mapper.Provider;
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
    public User checkUserExisted(Provider provider, UserInfo info) {
        try {
            return update(findPreAuthorizedUser(info), provider, info);
        } catch (IllegalArgumentException ignored) {
            return createNewUser(provider, info);
        }
    }

    private User update(User user, Provider provider, UserInfo info) {
        user.setEmail(info.email());
        user.setUsername(info.username());
        user.setAvatarUrl(info.avatarUrl());

        changeProviderIds(user, provider, info);

        return userRepository.save(user);
    }

    private User createNewUser(Provider provider, UserInfo info) {
        Role userRole = roleService.getUserRole();
        User newUser = User
                .builder()
                .email(info.email())
                .username(info.username())
                .avatarUrl(info.avatarUrl())
                .role(userRole)
                .build();

        changeProviderIds(newUser, provider, info);

        return userRepository.save(newUser);
    }

    private void changeProviderIds(User user, Provider provider, UserInfo info) {
        switch (provider) {
            case GIT_HUB -> user.setGitHubId(info.providerId());
            case GIT_LAB -> user.setGitLabId(info.providerId());
        }
    }

    private User findPreAuthorizedUser(UserInfo info) {
        return userRepository.findByEmailOrProviderId(info.email(), info.providerId()).orElseThrow(IllegalArgumentException::new);
    }

    public User findOne(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Пользователь с почтой \"%s\" не существует.".formatted(email)));
    }

    public Set<User> findAllByIds(List<Long> ids) {
        return userRepository.findAllByIds(ids);
    }
}
