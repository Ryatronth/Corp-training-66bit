package rnn.core.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rnn.core.model.admin.Answer;
import rnn.core.model.admin.Content;
import rnn.core.model.admin.content.DetailedContent;
import rnn.core.model.admin.content.FreeformContent;
import rnn.core.model.user.UserContent;
import rnn.core.model.user.UserCourse;
import rnn.core.model.user.UserModule;
import rnn.core.model.user.UserTopic;
import rnn.core.model.user.dto.UserContentDTO;
import rnn.core.model.user.repository.UserContentRepository;
import rnn.core.service.admin.ContentService;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserContentService {
    private final UserContentRepository userContentRepository;
    private final UserTopicService userTopicService;
    private final ContentService contentService;

    @Transactional
    public UserContent answer(long userTopicId, long contentId, int currentAttempts, List<String> answers) {
        Content content = contentService.findWithRightAnswers(contentId);
        UserTopic userTopic = userTopicService.findWithModuleAndCourse(userTopicId);

        UserContent userContent = UserContent
                .builder()
                .content(content)
                .topic(userTopic)
                .currentAttempts(currentAttempts - 1)
                .build();

        return processContent(userContent, answers);
    }

    private UserContent processContent(UserContent userContent, List<String> answers) {
        if (userContent.getContent() instanceof FreeformContent freeformContent) {
            if (freeformContent instanceof DetailedContent detailedContent) {
                boolean isSuccess = isSuccess(detailedContent.getAnswers(), answers);

                if (isSuccess) {
                    userContent.setSuccess(true);
                    userContent.setCompleted(true);
                }

                if (userContent.getCurrentAttempts() == 0) {
                    userContent.setCompleted(true);
                }
            }

            userContent.setAnswer(answers.toString());

            UserTopic topic = userContent.getTopic();
            UserModule module = topic.getModule();
            UserCourse course = module.getCourse();

            if (userContent.isSuccess()) {
                topic.setCurrentScore(freeformContent.getScore() + topic.getCurrentScore());
                module.setCurrentScore(freeformContent.getScore() + module.getCurrentScore());
                course.setCurrentScore(freeformContent.getScore() + course.getCurrentScore());
            }

            if (userContent.isCompleted()) {
                topic.setCompletedContents(1 + topic.getCompletedContents());

                if (topic.getCompletedContents() == topic.getTopic().getCountContents()) {
                    topic.setCompleted(true);
                    module.setCompletedTopics(1 + module.getCompletedTopics());
                }

                if (module.getCompletedTopics() == module.getModule().getCountTopics()) {
                    module.setCompleted(true);
                    course.setCompletedModules(1 + course.getCompletedModules());
                }

                if (course.getCompletedModules() == course.getCourse().getCountModules()) {
                    course.setCompleted(true);
                }
            }
        }

        return userContentRepository.save(userContent);
    }

    private boolean isSuccess(List<Answer> trueAnswers, List<String> answers) {
        int countSuccess = 0;

        for (Answer trueAnswer : trueAnswers) {
            for (String answer : answers) {
                if (trueAnswer.getAnswer().equalsIgnoreCase(answer)) {
                    countSuccess++;
                    break;
                }
            }
        }

        return countSuccess == trueAnswers.size() && answers.size() == trueAnswers.size();
    }

    public List<UserContentDTO> findByTopicId(long userTopicId, long topicId) {
        List<Content> contents = contentService.findByTopicIdWithAnswers(topicId);
        List<UserContent> userContents = userContentRepository.findAllByTopicIdFetchContent(userTopicId);

        List<UserContentDTO> userContentDTOs = new ArrayList<>(contents.size());
        for (Content content : contents) {
            int l = 0;
            int r = userContents.size() - 1;

            boolean found = false;
            while (l <= r) {
                int mid = l + (r - l) / 2;
                UserContent userContent = userContents.get(mid);

                if (userContent.getContent().getId() == content.getId()) {
                    if (content instanceof DetailedContent detailedContent) {
                        List<Answer> answers = detailedContent.getAnswers();
                        answers.forEach(answer -> answer.setRight(false));
                        detailedContent.setAnswers(answers);

                        userContentDTOs.add(new UserContentDTO(detailedContent, userContent));
                    } else {
                        userContentDTOs.add(new UserContentDTO(content, userContent));
                    }
                    found = true;
                    break;
                } else if (userContent.getContent().getId() > content.getId()) {
                    r = mid - 1;
                } else {
                    l = mid + 1;
                }
            }

            if (!found) {
                userContentDTOs.add(new UserContentDTO(content, null));
            }
        }

        return userContentDTOs;
    }
}
