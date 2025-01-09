package rnn.core.event.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DeleteModuleEvent extends ApplicationEvent {
    private final long moduleId;

    public DeleteModuleEvent(Object source, long moduleId) {
        super(source);
        this.moduleId = moduleId;
    }
}
