package rnn.core.event.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DeleteContentEvent extends ApplicationEvent {
    private final long topicId;
    private final long contentId;
    private final int score;

    public DeleteContentEvent(Object source, long topicId, long contentId, int score) {
        super(source);
        this.topicId = topicId;
        this.contentId = contentId;
        this.score = score;
    }
}
