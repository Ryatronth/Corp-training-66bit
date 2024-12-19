package rnn.core.event.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import rnn.core.event.event.CreateGroupEvent;
import rnn.core.event.event.DeleteGroupEvent;
import rnn.core.service.admin.DeadlineService;
import rnn.core.service.user.UserCourseService;

@RequiredArgsConstructor
@Component
public class GroupEventListener {
    private final UserCourseService userCourseService;
    private final DeadlineService deadlineService;

    @EventListener
    public void handleCreateGroupEvent(CreateGroupEvent event) {
        userCourseService.createAll(event.getCourse(), event.getUsers());
        deadlineService.createAll(event.getGroup(), event.getDeadlines());
    }

    @EventListener
    public void handleDeleteGroupEvent(DeleteGroupEvent event) {
        userCourseService.deleteAllByCourseIdAndGroupId(event.getCourseId(), event.getGroupId());
    }
}
