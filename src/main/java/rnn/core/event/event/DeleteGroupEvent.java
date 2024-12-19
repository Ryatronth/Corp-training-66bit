package rnn.core.event.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DeleteGroupEvent extends ApplicationEvent {
    private final long groupId;
    private final long courseId;

    public DeleteGroupEvent(Object source, long courseId, long groupId) {
        super(source);
        this.groupId = groupId;
        this.courseId = courseId;
    }
}
