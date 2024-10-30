package rnn.core.security.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import rnn.core.security.model.User;
import rnn.core.security.authentication.UserInfo;
import rnn.core.security.authentication.mapper.OAuth2UserMapper;
import rnn.core.security.service.UserService;

@RequiredArgsConstructor
@RestController
public class AuthController {
    private final UserService userService;

    @GetMapping("/user/info")
    public User getUser(OAuth2AuthenticationToken token) {
        UserInfo info = OAuth2UserMapper.getUserInfo(token.getAuthorizedClientRegistrationId(), token.getPrincipal());
        return userService.getUser(info.username());
    }
}
