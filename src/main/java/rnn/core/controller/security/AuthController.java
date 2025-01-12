package rnn.core.controller.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import rnn.core.authentication.UserInfo;
import rnn.core.authentication.mapper.OAuth2UserMapper;
import rnn.core.authentication.mapper.Provider;
import rnn.core.model.security.User;
import rnn.core.service.security.UserService;

@RequiredArgsConstructor
@RestController
public class AuthController {
    private final UserService userService;

    @GetMapping("/userinfo")
    public ResponseEntity<User> getUser(OAuth2AuthenticationToken token) {
        Provider provider = Provider.fromRegistrationId(token.getAuthorizedClientRegistrationId());
        UserInfo info = OAuth2UserMapper.getUserInfo(provider, token.getPrincipal());
        return ResponseEntity.ok().body(userService.findOne(info.email()));
    }
}
