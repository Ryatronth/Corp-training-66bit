package rnn.core.event.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import rnn.core.event.event.CreateCourseEvent;
import rnn.core.service.admin.GroupService;

@RequiredArgsConstructor
@Component
public class CourseEventListener {
    private final GroupService groupService;

    @EventListener
    public void handleCreateCourseEvent(CreateCourseEvent event) {
        groupService.createDefaultGroup(event.getCourse());
    }
}
