package rnn.core.event.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DeleteTopicEvent extends ApplicationEvent {
    private final long topicId;

    public DeleteTopicEvent(Object source, long topicId) {
        super(source);
        this.topicId = topicId;
    }
}
