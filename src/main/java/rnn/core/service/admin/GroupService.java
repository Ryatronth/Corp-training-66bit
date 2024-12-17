package rnn.core.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rnn.core.event.event.CreateGroupEvent;
import rnn.core.model.admin.Course;
import rnn.core.model.admin.Group;
import rnn.core.model.admin.dto.GroupDTO;
import rnn.core.model.admin.dto.GroupWithDeadlinesDTO;
import rnn.core.model.admin.dto.GroupWithUsersDTO;
import rnn.core.model.admin.repository.GroupRepository;
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

        group = groupRepository.save(group);
        eventPublisher.publishEvent(new CreateGroupEvent(this, course, group, dto.deadlines(), group.getUsers()));
        return group;
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

    public Group getDefaultGroup(long courseId) {
        return groupRepository.findDefaultGroup(courseId).orElseThrow(
                () -> new RuntimeException("Курс с указанным id не найден либо у него отсутствует группа по умолчанию")
        );
    }

    public List<Group> findAll(long courseId) {
        return groupRepository.findAllByCourseId(courseId);
    }

    public GroupWithUsersDTO findWithUsers(long groupId) {
        Group group = groupRepository.findByIdWithUsers(groupId).orElseThrow(() -> new RuntimeException("Группа с указанным id не найдена"));
        return GroupWithUsersDTO
                .builder()
                .group(group)
                .users(group.getUsers())
                .build();
    }

    public GroupWithDeadlinesDTO findWithDeadline(long groupId) {
        Group group = groupRepository.findByIdWithDeadlines(groupId).orElseThrow(() -> new RuntimeException("Группа с указанным id не найдена"));
        return GroupWithDeadlinesDTO
                .builder()
                .group(group)
                .deadlines(group.getDeadlines())
                .build();
    }
}
