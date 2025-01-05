package rnn.core.service.general;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rnn.core.model.admin.Group;
import rnn.core.model.general.dto.SubscribeCourseDTO;
import rnn.core.model.security.User;
import rnn.core.model.user.UserCourse;
import rnn.core.service.admin.GroupService;
import rnn.core.service.security.UserService;
import rnn.core.service.user.UserCourseService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class CourseSubscribeService {
    private final UserService userService;
    private final GroupService groupService;
    private final UserCourseService userCourseService;

    @Transactional
    public SubscribeCourseDTO subscribeCourse(long courseId, List<String> usernames) {
        Set<User> users = userService.findAllByUsernames(usernames);
        Group defaultGroup = groupService.addUsersInDefaultGroup(courseId, users);
        List<UserCourse> userCourses = userCourseService.createAll(defaultGroup.getCourse(), users);

        List<SubscribeCourseDTO.UserWithUserCourseDTO> usersDTOs = new ArrayList<>(userCourses.size());
        for (UserCourse userCourse : userCourses) {
            usersDTOs.add(new SubscribeCourseDTO.UserWithUserCourseDTO(userCourse.getUser(), userCourse));
        }

        return new SubscribeCourseDTO(defaultGroup.getCourse(), defaultGroup, usersDTOs);
    }
}
