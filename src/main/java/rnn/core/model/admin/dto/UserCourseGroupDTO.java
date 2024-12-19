package rnn.core.model.admin.dto;

import rnn.core.model.admin.Group;
import rnn.core.model.security.User;

public record UserCourseGroupDTO(User user, Group group, int currentScore) {
}
