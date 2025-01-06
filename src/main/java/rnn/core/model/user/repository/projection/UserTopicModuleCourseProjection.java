package rnn.core.model.user.repository.projection;

import rnn.core.model.user.UserCourse;
import rnn.core.model.user.UserModule;
import rnn.core.model.user.UserTopic;

public interface UserTopicModuleCourseProjection {
    UserTopic getUserTopic();
    UserModule getUserModule();
    UserCourse getUserCourse();
}
