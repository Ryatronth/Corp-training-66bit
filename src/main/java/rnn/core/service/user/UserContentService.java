package rnn.core.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rnn.core.model.admin.Answer;
import rnn.core.model.admin.Content;
import rnn.core.model.admin.content.DetailedContent;
import rnn.core.model.admin.content.FreeformContent;
import rnn.core.model.user.*;
import rnn.core.model.user.dto.AnswerDTO;
import rnn.core.model.user.dto.UserContentDTO;
import rnn.core.model.user.repository.UserContentRepository;
import rnn.core.model.user.repository.projection.UserContentTopicModuleCourseProjection;
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
    public AnswerDTO answer(long userTopicId, long contentId, int currentAttempts, List<String> answers) {
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

    private AnswerDTO processContent(UserContent userContent, List<String> answers) {
        UserTopic topic = userContent.getTopic();
        UserModule module = topic.getModule();
        UserCourse course = module.getCourse();

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

            if (userContent.isSuccess()) {
                topic.setCurrentScore(freeformContent.getScore() + topic.getCurrentScore());
                module.setCurrentScore(freeformContent.getScore() + module.getCurrentScore());
                course.setCurrentScore(freeformContent.getScore() + course.getCurrentScore());
            }

            if (userContent.isCompleted()) {
                topic.setCountAnsweredContents(topic.getCountAnsweredContents() + 1);

                if (topic.getCountAnsweredContents() == topic.getTopic().getCountAnsweredContents()) {
                    topic.setCompleted(true);
                    if (module.getCountTopics() < module.getModule().getCountTopics()) {
                        module.setCountTopics(module.getCountTopics() + 1);
                    }

                    if (module.getCountTopics() == module.getModule().getCountTopics()) {
                        module.setCompleted(true);
                        if (course.getCountModules() < course.getCourse().getCountModules()) {
                            course.setCountModules(course.getCountModules() + 1);
                        }

                        if (course.getCountModules() == course.getCourse().getCountModules()) {
                            course.setStatus(CourseStatus.FINISHED);
                        }
                    }
                }
            }
        }

        UserContent content = userContentRepository.save(userContent);
        return new AnswerDTO(course, module, topic, content);
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

    public List<UserContent> findAllSuccessByContentIdWithUserModuleAndCourse(long contentId) {
        return userContentRepository.findAllSuccessByContentIdFetchUserTopicModuleAndCourse(contentId);
    }

    public List<UserContentTopicModuleCourseProjection> findAllCompletedByContentIdWithUserModuleAndCourse(long contentId) {
        return userContentRepository.findAllCompletedByContentIdFetchUserTopicModuleAndCourse(contentId);
    }
}
