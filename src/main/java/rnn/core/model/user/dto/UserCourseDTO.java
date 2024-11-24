package rnn.core.model.user.dto;

import lombok.Builder;
import rnn.core.model.admin.Course;
import rnn.core.model.admin.Group;

@Builder
public record UserCourseDTO(long id, int currentScore, Course course, Group group) {
}
