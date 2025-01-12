package rnn.core.event.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DeleteFileContentEvent extends ApplicationEvent {
    private final String fileUrl;

    public DeleteFileContentEvent(Object source, String fileUrl) {
        super(source);
        this.fileUrl = fileUrl;
    }
}
