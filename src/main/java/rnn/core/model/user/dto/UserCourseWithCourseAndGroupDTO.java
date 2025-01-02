package rnn.core.model.user.dto;

import rnn.core.model.admin.Course;
import rnn.core.model.admin.Group;
import rnn.core.model.user.UserCourse;

public record UserCourseWithCourseAndGroupDTO(Course course, UserCourse userCourse, Group group) {
}
