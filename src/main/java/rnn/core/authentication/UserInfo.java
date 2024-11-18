package rnn.core.authentication;

import lombok.Builder;

@Builder
public record UserInfo(String username, String email, String avatarUrl) {
}
