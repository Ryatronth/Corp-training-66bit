package rnn.core.model.user.dto;

import lombok.Builder;
import rnn.core.model.admin.Course;
import rnn.core.model.admin.Group;

@Builder
public record UserCourseWithCourseDTO(Course course, Group group, int currentScore) {
}
