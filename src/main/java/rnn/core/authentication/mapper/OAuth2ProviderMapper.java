package rnn.core.authentication.mapper;

import org.springframework.security.oauth2.core.user.OAuth2User;
import rnn.core.authentication.UserInfo;

public interface OAuth2ProviderMapper {
    UserInfo map(OAuth2User principal);
}
