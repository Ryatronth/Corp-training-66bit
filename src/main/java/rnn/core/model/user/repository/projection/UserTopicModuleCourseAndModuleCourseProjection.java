package rnn.core.model.user.repository.projection;

import rnn.core.model.admin.Course;
import rnn.core.model.user.UserCourse;
import rnn.core.model.user.UserModule;
import rnn.core.model.user.UserTopic;

public interface UserTopicModuleCourseAndModuleCourseProjection {
    UserTopic getUserTopic();
    UserModule getUserModule();
    UserCourse getUserCourse();
    Module getModule();
    Course getCourse();
}
