package rnn.core.event.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class RollbackFileContentEvent extends ApplicationEvent {
    private final String fileUrl;

    public RollbackFileContentEvent(Object source, String fileUrl) {
        super(source);
        this.fileUrl = fileUrl;
    }
}
