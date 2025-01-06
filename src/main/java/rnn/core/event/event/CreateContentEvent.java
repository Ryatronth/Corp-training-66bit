package rnn.core.event.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CreateContentEvent extends ApplicationEvent {
    private final long topicId;
    private final int score;

    public CreateContentEvent(Object source, long topicId, int score) {
        super(source);
        this.topicId = topicId;
        this.score = score;
    }
}
