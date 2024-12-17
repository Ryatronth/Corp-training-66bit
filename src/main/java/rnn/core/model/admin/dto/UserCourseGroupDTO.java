package rnn.core.model.admin.dto;

import rnn.core.model.security.Role;

public record UserCourseGroupDTO(String username, String email, Role.Name role, String group, int currentScore) {
}
