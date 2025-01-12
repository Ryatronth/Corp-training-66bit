package rnn.core.authentication.mapper.impl;

import org.springframework.security.oauth2.core.user.OAuth2User;
import rnn.core.authentication.UserInfo;
import rnn.core.authentication.mapper.OAuth2ProviderMapper;

public class GitLabMapper implements OAuth2ProviderMapper {
    @Override
    public UserInfo map(OAuth2User principal) {
        String providerId = principal.getAttribute("sub");
        String username = principal.getAttribute("preferred_username");
        String email = principal.getAttribute("email");
        String avatarUrl = principal.getAttribute("picture");
        return UserInfo
                .builder()
                .providerId(providerId)
                .username(username)
                .email(email)
                .avatarUrl(avatarUrl)
                .build();
    }
}
