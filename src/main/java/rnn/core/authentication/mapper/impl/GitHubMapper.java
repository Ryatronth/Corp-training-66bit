package rnn.core.authentication.mapper.impl;

import org.springframework.security.oauth2.core.user.OAuth2User;
import rnn.core.authentication.UserInfo;
import rnn.core.authentication.mapper.OAuth2ProviderMapper;

public class GitHubMapper implements OAuth2ProviderMapper {
    @Override
    public UserInfo map(OAuth2User principal) {
        String username = principal.getAttribute("login");
        String email = principal.getAttribute("email");
        String avatarUrl = principal.getAttribute("avatar_url");
        return UserInfo
                .builder()
                .username(username)
                .email(email)
                .avatarUrl(avatarUrl)
                .build();
    }
}
