package rnn.core.model.admin.dto;

import rnn.core.model.security.User;

public record UserGroupDTO(User user, boolean inGroup) {
}
