package rnn.core.event.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import rnn.core.event.event.CreateCourseEvent;
import rnn.core.event.event.DeleteCourseEvent;
import rnn.core.service.admin.GroupService;
import rnn.core.service.filestorage.FileService;

@RequiredArgsConstructor
@Component
public class CourseEventListener {
    private final GroupService groupService;
    private final FileService fileService;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    public void handleCreateCourseEvent(CreateCourseEvent event) {
        groupService.createDefaultGroup(event.getCourse());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void handleRollbackCreateCourseEvent(CreateCourseEvent event) {
        fileService.deleteCourseImage(event.getCourse().getPictureUrl());
    }

    @Async
    @TransactionalEventListener
    public void handleDeleteCourseEvent(DeleteCourseEvent event) {
        fileService.deleteCourseImage(event.getImageUrl());
    }
}
