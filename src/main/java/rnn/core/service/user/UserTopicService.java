package rnn.core.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rnn.core.model.user.UserTopic;
import rnn.core.model.user.repository.UserTopicRepository;
import rnn.core.service.admin.TopicService;

import java.util.ArrayList;

@RequiredArgsConstructor
@Service
public class UserTopicService {
    private final UserTopicRepository userTopicRepository;
    private final UserModuleService userModuleService;
    private final TopicService topicService;

    public UserTopic create(long userModuleId, long topicId) {
        UserTopic userTopic = UserTopic
                .builder()
                .topic(topicService.find(topicId))
                .module(userModuleService.find(userModuleId))
                .currentScore(0)
                .contents(new ArrayList<>())
                .build();
        return userTopicRepository.save(userTopic);
    }

    public UserTopic find(long id) {
        return userTopicRepository.findById(id).orElseThrow(() -> new RuntimeException("Пользовательская тема с данным id не найдена"));
    }
}
