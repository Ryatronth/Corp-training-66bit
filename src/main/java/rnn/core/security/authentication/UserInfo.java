package rnn.core.security.authentication;

import lombok.Builder;

@Builder
public record UserInfo(String username, String email, String avatarUrl) {
}
