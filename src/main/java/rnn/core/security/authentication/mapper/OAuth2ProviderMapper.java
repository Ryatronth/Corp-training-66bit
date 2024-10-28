package rnn.core.security.authentication.mapper;

import org.springframework.security.oauth2.core.user.OAuth2User;
import rnn.core.security.authentication.UserInfo;

public interface OAuth2ProviderMapper {
    UserInfo map(OAuth2User principal);
}
