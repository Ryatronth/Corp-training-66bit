package rnn.core.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rnn.core.model.admin.Course;
import rnn.core.model.admin.Group;
import rnn.core.model.user.UserCourse;
import rnn.core.model.user.dto.UserCourseWithCourseDTO;
import rnn.core.model.user.repository.UserCourseRepository;
import rnn.core.service.admin.CourseService;
import rnn.core.service.security.UserService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserCourseService {
    private final UserService userService;
    private final CourseService courseService;
    private final UserCourseRepository userCourseRepository;

    public UserCourse create(String username, Group group) {
        return UserCourse
                .builder()
                .user(userService.findOne(username))
                .group(group)
                .build();
    }

    public List<UserCourseWithCourseDTO> findAllForUser(String username) {
        return userCourseRepository.findByUserUsername(username).stream().map(uc ->
                UserCourseWithCourseDTO
                        .builder()
                        .course(uc.getGroup().getCourse())
                        .group(uc.getGroup())
                        .currentScore(uc.getCurrentScore())
                        .build()
        ).toList();
    }

    public List<Course> findAllNotEnrolledByUser(String username) {
        return courseService.findAllNotEnrolledByUser(username);
    }
}
