package rnn.core.security.authentication.mapper;

import lombok.experimental.UtilityClass;
import org.springframework.security.oauth2.core.user.OAuth2User;
import rnn.core.security.authentication.UserInfo;
import rnn.core.security.authentication.mapper.impl.GitHubMapper;
import rnn.core.security.authentication.mapper.impl.GitLabMapper;

@UtilityClass
public class OAuth2UserMapper {
    public static UserInfo getUserInfo(String registrationId, OAuth2User oAuth2User) {
        OAuth2ProviderMapper mapper = OAuth2UserMapper.getMapper(registrationId);
        return mapper.map(oAuth2User);
    }

    public static OAuth2ProviderMapper getMapper(String authProvider) {
        return switch (authProvider) {
            case "github" -> new GitHubMapper();
            case "gitlab" -> new GitLabMapper();
            default -> throw new IllegalArgumentException("Unknown auth provider: " + authProvider);
        };
    }
}
