package rnn.core.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rnn.core.event.event.CreateGroupEvent;
import rnn.core.model.admin.Course;
import rnn.core.model.admin.Group;
import rnn.core.model.admin.GroupDeadline;
import rnn.core.model.admin.dto.DeadlineDTO;
import rnn.core.model.admin.dto.GroupDTO;
import rnn.core.model.admin.dto.GroupWithDeadlinesDTO;
import rnn.core.model.admin.dto.GroupWithUsersDTO;
import rnn.core.model.admin.repository.GroupRepository;
import rnn.core.model.security.User;
import rnn.core.service.security.UserService;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GroupService {
    private final ApplicationEventPublisher eventPublisher;

    private final GroupRepository groupRepository;
    private final CourseService courseService;
    private final UserService userService;

    @Transactional
    public Group create(long courseId, GroupDTO dto) {
        Course course = courseService.find(courseId);

        Group group = Group
                .builder()
                .course(course)
                .name(dto.name())
                .users(new ArrayList<>())
                .build();

        for (String username : dto.usernames()) {
            group.getUsers().add(userService.findOne(username));
        }

        group.setCountMembers(group.getUsers().size());

        group = groupRepository.save(group);
        eventPublisher.publishEvent(new CreateGroupEvent(this, course, group, dto.deadlines(), group.getUsers()));
        return group;
    }

    @Transactional
    public Group addUsersInDefaultGroup(long courseId, List<String> usernames) {
        Group defaultGroup = getDefaultGroup(courseId);
        for (String username : usernames) {
            defaultGroup.getUsers().add(userService.findOne(username));
        }
        return groupRepository.save(defaultGroup);
    }

    @Transactional
    public Group addUsersInGroup(long groupId, List<String> usernames) {
        Group group = findGroupWithUsers(groupId);
        for (String username : usernames) {
            group.getUsers().add(userService.findOne(username));
        }
        return groupRepository.save(group);
    }

    @Transactional
    public Group changeDeadlines(long groupId, List<DeadlineDTO> deadlines) {
        Group group = findWithDeadlines(groupId);
        for (DeadlineDTO dto : deadlines) {
            for (GroupDeadline deadline : group.getDeadlines()) {
                if (deadline.getModule().getId() == dto.moduleId()) {
                    deadline.setStartTime(dto.startTime());
                    deadline.setEndTime(dto.endTime());
                }
            }
        }
        return groupRepository.save(group);
    }

    @Transactional
    public Group createDefaultGroup(Course course) {
        Group group = Group
                .builder()
                .name("Группа по умолчанию")
                .course(course)
                .isDefault(true)
                .build();
        return groupRepository.save(group);
    }

    private Group getDefaultGroup(long courseId) {
        return groupRepository.findDefaultGroup(courseId).orElseThrow(
                () -> new RuntimeException("Курс с указанным id не найден либо у него отсутствует группа по умолчанию")
        );
    }

    public List<Group> findAll(long courseId) {
        return groupRepository.findAllByCourseId(courseId);
    }

    private Group findGroupWithUsers(long groupId) {
        return groupRepository.findByIdWithUsers(groupId).orElseThrow(() -> new RuntimeException("Группа с указанным id не найдена"));
    }

    public GroupWithUsersDTO findWithUsers(long groupId) {
        Group group = findGroupWithUsers(groupId);
        return GroupWithUsersDTO
                .builder()
                .group(group)
                .users(group.getUsers())
                .build();
    }

    private Group findWithDeadlines(long groupId) {
        return groupRepository.findByIdWithDeadlines(groupId).orElseThrow(() -> new RuntimeException("Группа с указанным id не найдена"));
    }

    public GroupWithDeadlinesDTO findWithDeadline(long groupId) {
        Group group = findWithDeadlines(groupId);
        return GroupWithDeadlinesDTO
                .builder()
                .group(group)
                .deadlines(group.getDeadlines())
                .build();
    }

    @Transactional
    public void deleteAndMove(long courseId, long groupId) {
        Group group = findGroupWithUsers(groupId);
        Group defaultGroup = getDefaultGroup(courseId);

        addUsersInDefaultGroup(defaultGroup, group.getUsers());
        deleteAndExclude(groupId);
    }

    @Transactional
    protected Group addUsersInDefaultGroup(Group defaultGroup, List<User> users) {
        for (User user : users) {
            defaultGroup.getUsers().add(user);
        }
        defaultGroup.setCountMembers(defaultGroup.getUsers().size());
        return groupRepository.save(defaultGroup);
    }

    public void deleteAndExclude(long groupId) {
        groupRepository.deleteById(groupId);
    }
}
