package rnn.core.security.authentication;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import rnn.core.model.User;
import rnn.core.security.authentication.mapper.OAuth2UserMapper;
import rnn.core.service.UserService;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final UserService userService;

    @Value("${spring.security.oauth2.default_success_url}")
    public String defaultSuccessUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

        UserInfo info = OAuth2UserMapper.getUserInfo(oauthToken.getAuthorizedClientRegistrationId(), oauthToken.getPrincipal());
        User user = userService.checkUserExisted(info);

        Set<GrantedAuthority> authorities = new HashSet<>(oauthToken.getAuthorities());
        authorities.add(user.getRole());

        OAuth2AuthenticationToken newAuth = new OAuth2AuthenticationToken(
                oauthToken.getPrincipal(),
                authorities,
                oauthToken.getAuthorizedClientRegistrationId()
        );

        SecurityContextHolder.getContext().setAuthentication(newAuth);

        response.sendRedirect(defaultSuccessUrl);
    }
}
