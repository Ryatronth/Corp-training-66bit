package rnn.core.event.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import rnn.core.model.admin.Course;

@Getter
public class CreateCourseEvent extends ApplicationEvent {
    private final Course course;

    public CreateCourseEvent(Object source, Course course) {
        super(source);
        this.course = course;
    }
}
