package rnn.core.authentication;

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
import rnn.core.authentication.mapper.OAuth2UserMapper;
import rnn.core.authentication.mapper.Provider;
import rnn.core.exception.MissingEmailException;
import rnn.core.model.security.User;
import rnn.core.service.security.UserService;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final UserService userService;

    @Value("${spring.security.oauth2.default_success_url}")
    public String defaultSuccessUrl;

    @Value("${spring.security.oauth2.default_auth_error_url}")
    public String defaultErrorUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        try {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

            Provider provider = Provider.fromRegistrationId(oauthToken.getAuthorizedClientRegistrationId());

            UserInfo info = OAuth2UserMapper.getUserInfo(provider, oauthToken.getPrincipal());
            User user = userService.checkUserExisted(provider, info);

            Set<GrantedAuthority> authorities = new HashSet<>(oauthToken.getAuthorities());
            authorities.add(user.getRole());

            OAuth2AuthenticationToken newAuth = new OAuth2AuthenticationToken(
                    oauthToken.getPrincipal(),
                    authorities,
                    oauthToken.getAuthorizedClientRegistrationId()
            );

            SecurityContextHolder.getContext().setAuthentication(newAuth);

            response.sendRedirect(defaultSuccessUrl);
        } catch (MissingEmailException e) {
            request.getSession().invalidate();
            response.sendRedirect(defaultErrorUrl + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
        }
    }
}
