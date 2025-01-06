package rnn.core.model.user.repository.projection;

import rnn.core.model.user.UserContent;
import rnn.core.model.user.UserCourse;
import rnn.core.model.user.UserModule;
import rnn.core.model.user.UserTopic;

public interface UserContentTopicModuleCourseProjection {
    UserContent getUserContent();
    UserTopic getUserTopic();
    UserModule getUserModule();
    UserCourse getUserCourse();
}
