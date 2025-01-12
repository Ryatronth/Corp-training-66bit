package rnn.core.event.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DeleteCourseEvent extends ApplicationEvent {
    private final String imageUrl;

    public DeleteCourseEvent(Object source, String imageUrl) {
        super(source);
        this.imageUrl = imageUrl;
    }
}
