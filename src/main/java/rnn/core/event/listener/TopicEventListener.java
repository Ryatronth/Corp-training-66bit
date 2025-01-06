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
import rnn.core.service.user.UserTopicService;

import java.util.List;

@RequiredArgsConstructor
@Component
public class TopicEventListener {
    private final UserTopicService userTopicService;

    @Transactional
    @EventListener
    public void handleDeleteEvent(DeleteTopicEvent event) {
        List<UserTopic> userTopics = userTopicService.findAllByTopicIdWithModuleCourseUserModuleAndCourse(event.getTopicId());

        for (UserTopic userTopic : userTopics) {
            UserModule userModule = userTopic.getModule();
            UserCourse userCourse = userModule.getCourse();

            if (userModule.isCompleted()) {
                userModule.setCountTopics(userModule.getCountTopics() - 1);
            }

            if (userModule.getCountTopics() == userModule.getModule().getCountTopics()) {
                userModule.setCompleted(true);
                userCourse.setCountModules(userCourse.getCountModules() + 1);

                if (userCourse.getCountModules() == userCourse.getCourse().getCountModules()) {
                    userCourse.setStatus(CourseStatus.FINISHED);
                }
            }

            userModule.setCurrentScore(userModule.getCurrentScore() - userTopic.getCurrentScore());
            userCourse.setCurrentScore(userCourse.getCurrentScore() - userTopic.getCurrentScore());
        }
    }
}
