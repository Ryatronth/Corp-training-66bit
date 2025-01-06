package rnn.core.model.admin.dto;

import rnn.core.model.admin.Group;
import rnn.core.model.security.User;
import rnn.core.model.user.CourseStatus;

public record UserCourseGroupDTO(User user, CourseStatus status, Group group, int currentScore) {
}
