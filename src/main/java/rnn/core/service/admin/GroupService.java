package rnn.core.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rnn.core.model.admin.Course;
import rnn.core.model.admin.Group;
import rnn.core.model.admin.GroupDeadline;
import rnn.core.model.admin.dto.DeadlineDTO;
import rnn.core.model.admin.dto.GroupDTO;
import rnn.core.model.admin.dto.GroupWithUsersDTO;
import rnn.core.model.admin.repository.GroupRepository;
import rnn.core.model.user.UserCourse;
import rnn.core.service.user.UserCourseService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GroupService {
    private final DeadlineService deadlineService;
    private final CourseService courseService;
    private final UserCourseService userCourseService;
    private final GroupRepository groupRepository;

    @Transactional
    public Group create(long courseId, GroupDTO groupDTO) {
        Course course = courseService.find(courseId);

        LocalDateTime startTime = LocalDateTime.MAX;
        LocalDateTime endTime = LocalDateTime.MIN;

        Group group = Group
                .builder()
                .name(groupDTO.name())
                .course(course)
                .build();

        List<UserCourse> userCourses = new ArrayList<>();
        for (String username : groupDTO.usernames()) {
            userCourses.add(userCourseService.create(username, group));
        }

        List<GroupDeadline> deadlines = new ArrayList<>();
        for (DeadlineDTO deadlineDTO : groupDTO.deadlines()) {
            if (startTime.isAfter(deadlineDTO.startTime())) {
                startTime = deadlineDTO.startTime();
            }
            if (endTime.isBefore(deadlineDTO.endTime())) {
                endTime = deadlineDTO.endTime();
            }

            deadlines.add(deadlineService.create(group, deadlineDTO));
        }

        group.setUserCourses(userCourses);
        group.setDeadlines(deadlines);
        group.setStartTime(startTime);
        group.setEndTime(endTime);

        return groupRepository.save(group);
    }

    public Group addUsers(long groupId, List<String> usernames) {
        Group group = findByIdWithUserCourses(groupId);

        List<UserCourse> userCourses = group.getUserCourses();
        for (String username : usernames) {
            userCourses.add(userCourseService.create(username, group));
        }

        group.setUserCourses(userCourses);
        return groupRepository.save(group);
    }

    public void delete(long groupId) {
        groupRepository.deleteById(groupId);
    }

    @Transactional
    public List<Group> findAll(long courseId) {
        return groupRepository.findByCourseId(courseId);
    }

    @Transactional
    public GroupWithUsersDTO findByIdWithUsers(long groupId) {
        Group group = findByIdWithUserCourses(groupId);
        return GroupWithUsersDTO
                .builder()
                .id(group.getId())
                .name(group.getName())
                .endTime(group.getEndTime())
                .startTime(group.getStartTime())
                .course(group.getUserCourses())
                .build();
    }

    public Group findByIdWithUserCourses(long groupId) {
        return groupRepository.findByIdWithUsers(groupId).orElseThrow(() -> new IllegalArgumentException("Группа с id = %s не найдена".formatted(groupId)));
    }

    public Group find(long groupId) {
        return groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("Группа с id = %s не найдена".formatted(groupId)));
    }
}
