package rnn.core.event.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rnn.core.event.event.DeleteModuleEvent;
import rnn.core.model.user.CourseStatus;
import rnn.core.model.user.UserCourse;
import rnn.core.model.user.UserModule;
import rnn.core.model.user.repository.projection.UserModuleCourseProjection;
import rnn.core.service.user.UserModuleService;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ModuleEventListener {
    private final UserModuleService userModuleService;

    @Transactional
    @EventListener
    public void handleDeleteEvent(DeleteModuleEvent event) {
        List<UserModuleCourseProjection> userModules = userModuleService.findAllByModuleIdWithUserCourseAndCourse(event.getModuleId());

        for (UserModuleCourseProjection projection : userModules) {
            UserModule userModule = projection.getUserModule();
            UserCourse userCourse = projection.getUserCourse();

            if (userModule != null) {
                userCourse.setCountAnsweredContents(userCourse.getCountAnsweredContents() - userModule.getCountAnsweredContents());
                userCourse.setCurrentScore(userCourse.getCurrentScore() - userModule.getCurrentScore());
            }

            if (userCourse.getCountAnsweredContents() == projection.getCourse().getCountAnsweredContents()) {
                if (!userCourse.getStatus().equals(CourseStatus.NOT_STARTED)) {
                    userCourse.setStatus(CourseStatus.FINISHED);
                }
            }
        }
    }
}
