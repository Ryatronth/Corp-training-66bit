package rnn.core.event.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import rnn.core.model.admin.Course;
import rnn.core.model.admin.Group;
import rnn.core.model.admin.dto.DeadlineDTO;
import rnn.core.model.security.User;

import java.util.List;
import java.util.Set;

@Getter
public class CreateGroupEvent extends ApplicationEvent {
    private final Course course;
    private final Group group;
    private final List<DeadlineDTO> deadlines;
    private final Set<User> users;

    public CreateGroupEvent(Object source, Course course, Group group, List<DeadlineDTO> deadlines, Set<User> users) {
        super(source);
        this.course = course;
        this.group = group;
        this.deadlines = deadlines;
        this.users = users;
    }
}
