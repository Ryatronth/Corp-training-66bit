package rnn.core.model.user.repository.projection;

import rnn.core.model.admin.Course;
import rnn.core.model.user.UserCourse;
import rnn.core.model.user.UserModule;

public interface UserModuleCourseProjection {
    UserModule getUserModule();
    UserCourse getUserCourse();
    Course getCourse();
}
