package rnn.core.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rnn.core.model.admin.Course;
import rnn.core.model.security.User;
import rnn.core.model.user.UserCourse;
import rnn.core.model.user.repository.UserCourseRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserCourseService {
    private final UserCourseRepository userCourseRepository;

    public UserCourse create(Course course, User user) {
        UserCourse userCourse = UserCourse
                .builder()
                .course(course)
                .user(user)
                .build();
        return userCourseRepository.save(userCourse);
    }

    public List<UserCourse> createAll(Course course, List<User> users) {
        List<UserCourse> userCourses = new ArrayList<>();
        for (User user : users) {
            UserCourse userCourse = UserCourse
                    .builder()
                    .course(course)
                    .user(user)
                    .build();
            userCourses.add(userCourse);
        }
        return userCourseRepository.saveAll(userCourses);
    }
}
