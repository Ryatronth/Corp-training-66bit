package rnn.core.model.general.dto;

import rnn.core.model.admin.Course;
import rnn.core.model.admin.Group;
import rnn.core.model.security.User;
import rnn.core.model.user.UserCourse;

import java.util.List;

public record SubscribeCourseDTO(Course course, Group group, List<UserWithUserCourseDTO> users) {
    public record UserWithUserCourseDTO(User user, UserCourse userCourse) {}
}
