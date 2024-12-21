package rnn.core.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import rnn.core.model.admin.Course;
import rnn.core.model.admin.repository.CourseRepository;
import rnn.core.model.security.User;
import rnn.core.model.user.UserCourse;
import rnn.core.model.user.dto.UserCourseDTO;
import rnn.core.model.user.repository.UserCourseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserCourseService {
    private final UserCourseRepository userCourseRepository;
    private final CourseRepository courseRepository;

    public Page<UserCourseDTO> findAllByUsernameWithCourse(String username, int page, int limit) {
        return userCourseRepository.findAllByUsernameWithCourse(username, PageRequest.of(page, limit));
    }

    public Page<Course> findAllNotEnrolled(String username, int page, int limit) {
        return courseRepository.findCoursesNotEnrolledByUser(username, PageRequest.of(page, limit));
    }

    public UserCourse create(Course course, User user) {
        UserCourse userCourse = UserCourse
                .builder()
                .course(course)
                .user(user)
                .build();
        return userCourseRepository.save(userCourse);
    }

    public void createAll(Course course, Set<User> users) {
        List<UserCourse> userCourses = new ArrayList<>();
        for (User user : users) {
            UserCourse userCourse = UserCourse
                    .builder()
                    .course(course)
                    .user(user)
                    .build();
            userCourses.add(userCourse);
        }
        userCourseRepository.saveAll(userCourses);
    }

    public void deleteAllByCourseIdAndUsernames(long courseId, List<String> username) {
        userCourseRepository.deleteAllByCourseIdAndUsernames(courseId, username);
    }

    public void deleteAllByCourseIdAndGroupId(long courseId, long groupId) {
        userCourseRepository.deleteAllByCourseIdAndGroupId(courseId, groupId);
    }
}
