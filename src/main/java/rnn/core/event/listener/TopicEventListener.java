package rnn.core.event.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rnn.core.event.event.DeleteTopicEvent;
import rnn.core.model.user.CourseStatus;
import rnn.core.model.user.UserCourse;
import rnn.core.model.user.UserModule;
import rnn.core.model.user.UserTopic;
import rnn.core.model.user.repository.projection.UserTopicModuleCourseAndModuleCourseProjection;
import rnn.core.service.user.UserTopicService;

import java.util.List;

@RequiredArgsConstructor
@Component
public class TopicEventListener {
    private final UserTopicService userTopicService;

    @Transactional
    @EventListener
    public void handleDeleteEvent(DeleteTopicEvent event) {
        List<UserTopicModuleCourseAndModuleCourseProjection> userTopics = userTopicService.findAllByTopicIdWithModuleCourseUserModuleAndCourse(event.getTopicId());

        for (UserTopicModuleCourseAndModuleCourseProjection projection : userTopics) {
            UserTopic userTopic = projection.getUserTopic();
            UserModule userModule = projection.getUserModule();
            UserCourse userCourse = projection.getUserCourse();

            if (userTopic != null) {
                userModule.setCountAnsweredContents(userModule.getCountAnsweredContents() - userTopic.getCountAnsweredContents());
                userCourse.setCountAnsweredContents(userCourse.getCountAnsweredContents() - userTopic.getCountAnsweredContents());

                userModule.setCurrentScore(userModule.getCurrentScore() - userTopic.getCurrentScore());
                userCourse.setCurrentScore(userCourse.getCurrentScore() - userTopic.getCurrentScore());
            }

            if (userModule != null && userModule.getCountAnsweredContents() == userModule.getModule().getCountAnsweredContents()) {
                userModule.setCompleted(true);
            }

            if (userCourse.getCountAnsweredContents() == userCourse.getCourse().getCountAnsweredContents()) {
                userCourse.setStatus(CourseStatus.FINISHED);
            }
        }
    }
}
