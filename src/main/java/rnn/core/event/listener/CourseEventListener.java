package rnn.core.event.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;
import rnn.core.event.event.CreateCourseEvent;
import rnn.core.service.admin.GroupService;

@RequiredArgsConstructor
@Component
public class CourseEventListener {
    private final GroupService groupService;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    public void handleCreateCourseEvent(CreateCourseEvent event) {
        groupService.createDefaultGroup(event.getCourse());
    }
}
