package rnn.core.event.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import rnn.core.model.admin.Course;
import rnn.core.model.security.User;

import java.util.Set;

@Getter
public class AddUserEvent extends ApplicationEvent {
    private final Course course;
    private final Set<User> users;

    public AddUserEvent(Object source, Course course, Set<User> users) {
        super(source);
        this.course = course;
        this.users = users;
    }
}
