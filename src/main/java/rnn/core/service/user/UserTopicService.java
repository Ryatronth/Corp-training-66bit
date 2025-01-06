package rnn.core.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rnn.core.model.admin.Topic;
import rnn.core.model.user.CourseStatus;
import rnn.core.model.user.UserCourse;
import rnn.core.model.user.UserModule;
import rnn.core.model.user.UserTopic;
import rnn.core.model.user.repository.UserTopicRepository;
import rnn.core.model.user.repository.projection.UserTopicModuleCourseProjection;
import rnn.core.service.admin.TopicService;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserTopicService {
    private final UserTopicRepository userTopicRepository;
    private final UserModuleService userModuleService;
    private final TopicService topicService;

    @Transactional
    public UserTopic create(long userModuleId, long topicId) {
        Topic topic = topicService.find(topicId);
        UserModule userModule = userModuleService.findWithModuleUserModuleCourseUserCourse(userModuleId);

        UserTopic userTopic = UserTopic
                .builder()
                .topic(topic)
                .module(userModule)
                .currentScore(0)
                .contents(new ArrayList<>(0))
                .build();

        if (topic.getCountAnsweredContents() == 0) {
            userTopic.setCompleted(true);
            userModule.setCountTopics(userModule.getCountTopics() + 1);

            if (userModule.getCountTopics() == userModule.getModule().getCountTopics()) {
                userModule.setCompleted(true);

                UserCourse userCourse = userModule.getCourse();
                userCourse.setCountModules(userCourse.getCountModules() + 1);

                if (userCourse.getCountModules() == userCourse.getCourse().getCountModules()) {
                    userCourse.setStatus(CourseStatus.FINISHED);
                }
            }
        }

        return userTopicRepository.save(userTopic);
    }

    public UserTopic findWithModuleAndCourse(long id) {
        return userTopicRepository.findByIdFetchModuleAndCourse(id).orElseThrow(() -> new RuntimeException("Пользовательская тема с данным id не найдена"));
    }

    public List<UserTopicModuleCourseProjection> findAllByTopicIdWithUserModuleAndCourse(long topicId) {
        return userTopicRepository.findAllByTopicIdFetchUserModuleAndCourse(topicId);
    }

    public List<UserTopic> findAllByTopicIdWithModuleCourseUserModuleAndCourse(long topicId) {
        return userTopicRepository.findAllByTopicIdFetchModuleCourseUserModuleAndCourse(topicId);
    }
}
