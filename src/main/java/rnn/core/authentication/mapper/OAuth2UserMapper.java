package rnn.core.authentication.mapper;

import lombok.experimental.UtilityClass;
import org.springframework.security.oauth2.core.user.OAuth2User;
import rnn.core.authentication.UserInfo;
import rnn.core.authentication.mapper.impl.GitHubMapper;
import rnn.core.authentication.mapper.impl.GitLabMapper;
import rnn.core.exception.MissingEmailException;

@UtilityClass
public class OAuth2UserMapper {
    public static UserInfo getUserInfo(Provider registrationId, OAuth2User oAuth2User) {
        OAuth2ProviderMapper mapper = OAuth2UserMapper.getMapper(registrationId);

        UserInfo userInfo = mapper.map(oAuth2User);
        if (userInfo.email() == null || userInfo.email().trim().isEmpty()) {
            throw new MissingEmailException("Укажите вашу почту в провайдере авторизации либо сделайте её публичной.");
        }

        return userInfo;
    }

    public static OAuth2ProviderMapper getMapper(Provider authProvider) {
        return switch (authProvider) {
            case GIT_HUB -> new GitHubMapper();
            case GIT_LAB -> new GitLabMapper();
        };
    }
}
