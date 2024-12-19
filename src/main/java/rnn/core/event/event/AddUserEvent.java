package rnn.core.event.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import rnn.core.model.admin.Course;
import rnn.core.model.security.User;

import java.util.List;

@Getter
public class AddUserEvent extends ApplicationEvent {
    private final Course course;
    private final List<User> users;

    public AddUserEvent(Object source, Course course, List<User> users) {
        super(source);
        this.course = course;
        this.users = users;
    }
}