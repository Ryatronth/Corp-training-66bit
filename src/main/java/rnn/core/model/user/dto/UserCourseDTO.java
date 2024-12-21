package rnn.core.model.user.dto;

import rnn.core.model.admin.Course;
import rnn.core.model.user.UserCourse;

public record UserCourseDTO(Course course, UserCourse userCourse) {
}
