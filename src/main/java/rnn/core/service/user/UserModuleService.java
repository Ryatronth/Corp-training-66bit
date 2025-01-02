package rnn.core.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rnn.core.model.admin.GroupDeadline;
import rnn.core.model.admin.Module;
import rnn.core.model.admin.Topic;
import rnn.core.model.user.UserModule;
import rnn.core.model.user.UserTopic;
import rnn.core.model.user.dto.UserModuleWithModuleDTO;
import rnn.core.model.user.repository.UserModuleRepository;
import rnn.core.service.admin.DeadlineService;
import rnn.core.service.admin.ModuleService;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserModuleService {
    private final UserModuleRepository userModuleRepository;
    private final ModuleService moduleService;
    private final UserCourseService userCourseService;
    private final DeadlineService deadlineService;

    public UserModule create(long userCourseId, long moduleId) {
        UserModule userModule = UserModule
                .builder()
                .module(moduleService.find(moduleId))
                .course(userCourseService.findById(userCourseId))
                .topics(new ArrayList<>())
                .currentScore(0)
                .build();
        return userModuleRepository.save(userModule);
    }

    public List<UserModuleWithModuleDTO> findAllWithModuleAndDeadline(long courseId, long userCourseId, long groupId) {
        List<Module> modules = moduleService.findByCourseIdFetchTopics(courseId);
        List<UserModule> userModules = userModuleRepository.findByCourseIdFetchTopics(userCourseId);
        List<GroupDeadline> deadlines = deadlineService.findAllByGroupIdFetchModule(groupId);

        List<UserModuleWithModuleDTO> userModuleWithModuleDTOs = new ArrayList<>();
        for (Module module : modules) {
            int l = 0;
            int r = userModules.size() - 1;

            boolean found = false;
            while (l <= r) {
                int mid = l + (r - l) / 2;
                UserModule userModule = userModules.get(mid);

                if (userModule.getModule().getId() == module.getId()) {
                    userModuleWithModuleDTOs.add(
                            UserModuleWithModuleDTO
                                    .builder()
                                    .module(module)
                                    .topics(matchTopics(module, userModule))
                                    .userModule(userModule)
                                    .deadline(matchDeadline(module, deadlines))
                                    .build());
                    found = true;
                    break;
                } else if (userModule.getModule().getId() > module.getId()) {
                    r = mid - 1;
                } else {
                    l = mid + 1;
                }
            }

            if (!found) {
                List<UserModuleWithModuleDTO.TopicWithUserTopicDTO> topics = new ArrayList<>();

                for (Topic topic : module.getTopics()) {
                    topics.add(new UserModuleWithModuleDTO.TopicWithUserTopicDTO(topic, null));
                }

                userModuleWithModuleDTOs.add(
                        UserModuleWithModuleDTO
                                .builder()
                                .module(module)
                                .topics(topics)
                                .deadline(matchDeadline(module, deadlines))
                                .build()
                );
            }
        }

        return userModuleWithModuleDTOs;
    }

    private GroupDeadline matchDeadline(Module module, List<GroupDeadline> deadlines) {
        int l = 0;
        int r = deadlines.size() - 1;

        while (l <= r) {
            int mid = l + (r - l) / 2;
            GroupDeadline groupDeadline = deadlines.get(mid);

            if (groupDeadline.getModule().getId() == module.getId()) {
                return groupDeadline;
            } else if (groupDeadline.getModule().getId() > module.getId()) {
                r = mid - 1;
            } else {
                l = mid + 1;
            }
        }

        return null;
    }

    private List<UserModuleWithModuleDTO.TopicWithUserTopicDTO> matchTopics(Module module, UserModule userModule) {
        List<UserModuleWithModuleDTO.TopicWithUserTopicDTO> topics = new ArrayList<>();

        List<UserTopic> userTopics = userModule.getTopics();
        for (Topic topic : module.getTopics()) {
            int l = 0;
            int r = userTopics.size();

            while (l <= r) {
                int mid = l + (r - l) / 2;
                UserTopic userTopic = userTopics.get(mid);

                if (userTopic.getTopic().getId() == topic.getId()) {
                    topics.add(new UserModuleWithModuleDTO.TopicWithUserTopicDTO(topic, userTopic));
                    break;
                } else if (userTopic.getTopic().getId() > topic.getId()) {
                    r = mid - 1;
                } else {
                    l = mid + 1;
                }
            }
        }

        return topics;
    }

    public UserModule find(long id) {
        return userModuleRepository.findById(id).orElseThrow(() -> new RuntimeException("Модуль пользователя с указанным id не найден"));
    }
}
