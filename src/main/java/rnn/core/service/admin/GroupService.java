package rnn.core.service.admin;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rnn.core.event.event.AddUserEvent;
import rnn.core.event.event.CreateGroupEvent;
import rnn.core.event.event.DeleteGroupEvent;
import rnn.core.event.event.DeleteUserEvent;
import rnn.core.model.admin.Course;
import rnn.core.model.admin.Group;
import rnn.core.model.admin.GroupDeadline;
import rnn.core.model.admin.QGroup;
import rnn.core.model.admin.dto.*;
import rnn.core.model.admin.repository.GroupRepository;
import rnn.core.model.querydsl.PageableBuilder;
import rnn.core.model.security.User;
import rnn.core.service.security.UserService;

import java.util.*;

@RequiredArgsConstructor
@Service
public class GroupService {
    private final ApplicationEventPublisher eventPublisher;
    private final JPAQueryFactory queryFactory;

    private final GroupRepository groupRepository;
    private final CourseService courseService;
    private final UserService userService;

    @Transactional
    public Group create(long courseId, GroupDTO dto) {
        Course course = courseService.find(courseId);
        Group defaultGroup = findDefaultGroupWithUsers(courseId);

        Group group = Group
                .builder()
                .course(course)
                .name(dto.name())
                .users(new HashSet<>())
                .build();

        Set<User> users = group.getUsers();
        Set<User> defaultUsers = defaultGroup.getUsers();

        Set<User> moveToTarget = new HashSet<>();
        Set<User> newUsers = new HashSet<>();

        Set<User> processUsers = userService.findAllByUsernames(dto.usernames());
        for (User user : processUsers) {
            if (defaultUsers.contains(user)) {
                moveToTarget.add(user);
            } else {
                newUsers.add(user);
            }
            users.add(user);
        }

        group.setUsers(users);
        group.setCountMembers(group.getUsers().size());

        group = groupRepository.save(group);
        eventPublisher.publishEvent(new CreateGroupEvent(this, course, group, dto.deadlines(), newUsers));

        return moveUsersBetweenGroups(defaultGroup, group, moveToTarget);
    }

    @Transactional
    public Group updateUsers(long courseId, long groupId, List<String> usernames) {
        Group group = groupRepository.findByIdWithUsersAndCourse(groupId).orElseThrow(
                () -> new RuntimeException("Группа с указанным id не найдена")
        );
        Group defaultGroup = findDefaultGroupWithUsers(courseId);

        Set<User> defaultGroupUsers = defaultGroup.getUsers();
        Set<User> users = group.getUsers();

        Set<User> moveToTarget = new HashSet<>();
        Set<User> newUsers = new HashSet<>();
        Set<User> toDelete = new HashSet<>();

        Set<User> processUsers = userService.findAllByUsernames(usernames);
        for (User user: users) {
            if (processUsers.contains(user)) {
                processUsers.remove(user);
            } else {
                toDelete.add(user);
            }
        }

        for (User user : processUsers) {
            if (defaultGroupUsers.contains(user)) {
                moveToTarget.add(user);
            } else {
                newUsers.add(user);
            }
        }

        users.removeAll(toDelete);
        users.addAll(newUsers);

        group.setCountMembers(users.size());

        eventPublisher.publishEvent(new AddUserEvent(this, group.getCourse(), newUsers));
        eventPublisher.publishEvent(new DeleteUserEvent(this, group.getCourse().getId(), toDelete.stream().map(User::getUsername).toList()));
        return moveUsersBetweenGroups(defaultGroup, group, moveToTarget);
    }

    @Transactional
    public Group moveUsers(long targetId, List<ProcessGroupsUsersDTO> dtos) {
        Map<Long, List<String>> groupToUsers = buildGroupToUsersMap(dtos);

        Group target = findGroupWithUsers(targetId);

        for (Map.Entry<Long, List<String>> entry : groupToUsers.entrySet()) {
            Group destination = findGroupWithUsers(entry.getKey());
            Set<User> users = userService.findAllByUsernames(entry.getValue());

            moveUsersBetweenGroups(target, destination, users);
        }

        return target;
    }

    private Group moveUsersBetweenGroups(Group destinationGroup, Group targetGroup, Set<User> users) {
        for (User user : users) {
            destinationGroup.getUsers().remove(user);
            targetGroup.getUsers().add(user);
        }

        destinationGroup.setCountMembers(destinationGroup.getUsers().size());
        targetGroup.setCountMembers(targetGroup.getUsers().size());

        groupRepository.save(destinationGroup);
        groupRepository.save(targetGroup);
        return targetGroup;
    }

    @Transactional
    public Group addUsersInDefaultGroup(long courseId, List<String> usernames) {
        Group defaultGroup = groupRepository.findDefaultGroupWithCourseAndUsers(courseId).orElseThrow(
                () -> new RuntimeException("Курс с указанным id не найден либо у него отсутствует группа по умолчанию")
        );

        Set<User> toAdd = userService.findAllByUsernames(usernames);
        defaultGroup.getUsers().addAll(toAdd);

        defaultGroup.setCountMembers(defaultGroup.getUsers().size());

        eventPublisher.publishEvent(new AddUserEvent(this, defaultGroup.getCourse(), toAdd));
        return groupRepository.save(defaultGroup);
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
    public void createDefaultGroup(Course course) {
        Group group = Group
                .builder()
                .name("Группа по умолчанию")
                .course(course)
                .isDefault(true)
                .build();
        groupRepository.save(group);
    }

    public Page<Group> findAll(long courseId, String sort, String direction, int page, int limit) {
        QGroup group = QGroup.group;
        JPAQuery<Group> query = queryFactory
                .selectFrom(group)
                .where(group.course.id.eq(courseId));

        if (sort != null && !sort.trim().isEmpty() && direction != null && !direction.trim().isEmpty()) {
            query.orderBy(createFindAllOrderSpecifier(sort, direction));
        }

        return PageableBuilder.build(query, page, limit);
    }

    private OrderSpecifier<?> createFindAllOrderSpecifier(String sort, String direction) {
        Order order = direction.equalsIgnoreCase("asc") ? Order.ASC : Order.DESC;

        return switch (sort) {
            case "name" -> new OrderSpecifier<>(order, QGroup.group.name);
            case "count" -> new OrderSpecifier<>(order, QGroup.group.countMembers);
            default -> throw new IllegalArgumentException("Invalid sort field: " + sort);
        };
    }

    private Group findGroupWithUsers(long groupId) {
        return groupRepository.findByIdWithUsers(groupId).orElseThrow(() -> new RuntimeException("Группа с указанным id не найдена"));
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
    public void deleteUsersFromGroup(long courseId, List<ProcessGroupsUsersDTO> dtos) {
        Map<Long, List<String>> groupToUsers = buildGroupToUsersMap(dtos);

        for (Map.Entry<Long, List<String>> entry : groupToUsers.entrySet()) {
            groupRepository.deleteUsersByUsernames(entry.getKey(), entry.getValue());

            eventPublisher.publishEvent(new DeleteUserEvent(this, courseId, entry.getValue()));
        }
    }

    private Map<Long, List<String>> buildGroupToUsersMap(List<ProcessGroupsUsersDTO> dtos) {
        Map<Long, List<String>> groupToUsers = new HashMap<>();

        for (ProcessGroupsUsersDTO dto : dtos) {
            List<String> usernames = groupToUsers.getOrDefault(dto.groupId(), new ArrayList<>());
            usernames.add(dto.username());
            groupToUsers.put(dto.groupId(), usernames);
        }

        return groupToUsers;
    }

    @Transactional
    public void deleteAndMove(long courseId, long groupId) {
        Group group = findGroupWithUsers(groupId);
        Group defaultGroup = findDefaultGroupWithUsers(courseId);

        addUsersInDefaultGroup(defaultGroup, group.getUsers());
        groupRepository.delete(group);
    }

    private Group findDefaultGroupWithUsers(long courseId) {
        return groupRepository.findDefaultGroupWithUsers(courseId).orElseThrow(
                () -> new RuntimeException("Курс с указанным id не найден либо у него отсутствует группа по умолчанию")
        );
    }

    @Transactional
    protected void addUsersInDefaultGroup(Group defaultGroup, Set<User> users) {
        for (User user : users) {
            defaultGroup.getUsers().add(user);
        }
        defaultGroup.setCountMembers(defaultGroup.getUsers().size());
        groupRepository.save(defaultGroup);
    }

    @Transactional
    public void deleteAndExclude(long courseId, long groupId) {
        eventPublisher.publishEvent(new DeleteGroupEvent(this, courseId, groupId));

        groupRepository.deleteById(groupId);
    }
}
