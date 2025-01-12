package rnn.core.event.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import rnn.core.event.event.*;
import rnn.core.model.admin.Course;
import rnn.core.model.admin.Module;
import rnn.core.model.admin.Topic;
import rnn.core.model.user.*;
import rnn.core.model.user.repository.projection.UserContentTopicModuleCourseProjection;
import rnn.core.model.user.repository.projection.UserTopicModuleCourseProjection;
import rnn.core.service.admin.TopicService;
import rnn.core.service.filestorage.FileService;
import rnn.core.service.user.UserContentService;
import rnn.core.service.user.UserTopicService;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ContentEventListener {
    private final TopicService topicService;
    private final UserTopicService userTopicService;
    private final UserContentService userContentService;
    private final FileService fileService;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    public void handleCreateEvent(CreateContentEvent event) {
        Topic topic = topicService.findWithModuleAndCourse(event.getTopicId());
        Module module = topic.getModule();
        Course course = module.getCourse();

        topic.setCountAnsweredContents(topic.getCountAnsweredContents() + 1);
        module.setCountAnsweredContents(module.getCountAnsweredContents() + 1);
        course.setCountAnsweredContents(course.getCountAnsweredContents() + 1);

        if (event.getScore() != 0) {
            changeScores(course, module, topic, event.getScore());
        }

        List<UserTopicModuleCourseProjection> userTopics = userTopicService.findAllByTopicIdWithUserModuleAndCourse(topic.getId(), module.getId());
        for (UserTopicModuleCourseProjection projection : userTopics) {
            UserTopic userTopic = projection.getUserTopic();
            if (userTopic != null) {
                userTopic.setCompleted(false);
            }

            UserModule userModule = projection.getUserModule();
            if (userModule != null) {
                userModule.setCompleted(false);
            }

            UserCourse userCourse = projection.getUserCourse();
            if (!userCourse.getStatus().equals(CourseStatus.NOT_STARTED)) {
                projection.getUserCourse().setStatus(CourseStatus.IN_PROGRESS);
            }
        }
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    public void handleUpdateEvent(UpdateContentEvent event) {
        Topic topic = topicService.findWithModuleAndCourse(event.getTopicId());
        Module module = topic.getModule();
        Course course = module.getCourse();

        if (event.getScore() != 0) {
            changeScores(course, module, topic, event.getScore());

            List<UserContent> userContents = userContentService.findAllSuccessByContentIdWithUserModuleAndCourse(event.getContentId());
            for (UserContent userContent : userContents) {
                UserTopic userTopic = userContent.getTopic();
                userTopic.setCurrentScore(userTopic.getCurrentScore() + event.getScore());

                UserModule userModule = userTopic.getModule();
                userModule.setCurrentScore(userModule.getCurrentScore() + event.getScore());

                UserCourse userCourse = userModule.getCourse();
                userCourse.setCurrentScore(userCourse.getCurrentScore() + event.getScore());
            }
        }
    }

    @Transactional
    @EventListener
    public void handleDeleteEvent(DeleteContentEvent event) {
        Topic topic = topicService.findWithModuleAndCourse(event.getTopicId());
        Module module = topic.getModule();
        Course course = module.getCourse();

        topic.setCountAnsweredContents(topic.getCountAnsweredContents() - 1);
        module.setCountAnsweredContents(module.getCountAnsweredContents() - 1);
        course.setCountAnsweredContents(course.getCountAnsweredContents() - 1);

        if (event.getScore() != 0) {
            changeScores(course, module, topic, event.getScore());
        }

        List<UserContentTopicModuleCourseProjection> userContents = userContentService.findAllByContentIdWithUserModuleAndCourse(event.getContentId());
        for (UserContentTopicModuleCourseProjection projection : userContents) {
            UserContent userContent = projection.getUserContent();
            UserTopic userTopic = projection.getUserTopic();
            UserModule userModule = projection.getUserModule();
            UserCourse userCourse = projection.getUserCourse();

            if (userContent != null) {
                if (userContent.isCompleted()) {
                    userTopic.setCountAnsweredContents(userTopic.getCountAnsweredContents() - 1);
                    userModule.setCountAnsweredContents(userModule.getCountAnsweredContents() - 1);
                    userCourse.setCountAnsweredContents(userCourse.getCountAnsweredContents() - 1);

                    if (userContent.isSuccess()) {
                        userCourse.setCurrentScore(userCourse.getCurrentScore() + event.getScore());
                        userModule.setCurrentScore(userModule.getCurrentScore() + event.getScore());
                        userTopic.setCurrentScore(userTopic.getCurrentScore() + event.getScore());
                    }
                }
            }

            if (userTopic != null) {
                if (userTopic.getCountAnsweredContents() == topic.getCountAnsweredContents()) {
                    userTopic.setCompleted(true);
                }
            }

            if (userModule != null) {
                if (userModule.getCountAnsweredContents() == module.getCountAnsweredContents()) {
                    userModule.setCompleted(true);
                }
            }

            if (userCourse.getCountAnsweredContents() == course.getCountAnsweredContents()) {
                if (!userCourse.getStatus().equals(CourseStatus.NOT_STARTED)) {
                    userCourse.setStatus(CourseStatus.FINISHED);
                }
            }
        }
    }

    @Async
    @TransactionalEventListener
    public void handleDeleteFileContentEvent(DeleteFileContentEvent event) {
        fileService.deleteContentFile(event.getFileUrl());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void handleRollbackFileContentEvent(RollbackFileContentEvent event) {
        fileService.deleteContentFile(event.getFileUrl());
    }

    private void changeScores(Course course, Module module, Topic topic, int score) {
        topic.setScore(topic.getScore() + score);
        module.setScore(module.getScore() + score);
        course.setScore(course.getScore() + score);
    }
}
