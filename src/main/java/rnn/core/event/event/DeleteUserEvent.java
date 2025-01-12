package rnn.core.event.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class DeleteUserEvent extends ApplicationEvent {
    private final long courseId;
    private final List<Long> userIds;

    public DeleteUserEvent(Object source, long courseId, List<Long> userIds) {
        super(source);
        this.courseId = courseId;
        this.userIds = userIds;
    }
}
