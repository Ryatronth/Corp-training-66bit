package rnn.core.event.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DeleteModuleEvent extends ApplicationEvent {
    private final long moduleId;
    private final int score;

    public DeleteModuleEvent(Object source, long moduleId, int score) {
        super(source);
        this.moduleId = moduleId;
        this.score = score;
    }
}
