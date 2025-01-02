package rnn.core.service.user;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import rnn.core.controller.admin.filter.CourseFilter;
import rnn.core.model.admin.Course;
import rnn.core.model.admin.QCourse;
import rnn.core.model.security.User;
import rnn.core.model.user.QUserCourse;
import rnn.core.model.user.UserCourse;
import rnn.core.model.user.dto.UserCourseDTO;
import rnn.core.model.user.dto.UserCourseWithCourseAndGroupDTO;
import rnn.core.model.user.repository.UserCourseRepository;
import rnn.core.querydsl.PageableBuilder;
import rnn.core.querydsl.course.CourseQueryFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserCourseService {
    private final JPAQueryFactory queryFactory;

    private final UserCourseRepository userCourseRepository;

    public UserCourseWithCourseAndGroupDTO findWithCourseAndGroup(long userCourseId, String username) {
        return userCourseRepository.findByIdWithCourseAndGroup(userCourseId, username);
    }

    public Page<UserCourseDTO> findAllByUsernameWithCourse(
            String username,
            String title,
            List<String> tags,
            CourseFilter filter,
            int page,
            int limit
    ) {
        QCourse course = QCourse.course;
        QUserCourse userCourse = QUserCourse.userCourse;

        JPAQuery<UserCourseDTO> query = queryFactory
                .select(Projections.constructor(
                        UserCourseDTO.class,
                        course,
                        userCourse
                ))
                .from(userCourse)
                .join(userCourse.course, course)
                .where(userCourse.user.username.eq(username))
                .orderBy(course.title.asc());

        query = CourseQueryFilter.filtrate(query, title, tags, filter);

        return PageableBuilder.build(query, page, limit);
    }

    public Page<Course> findAllNotEnrolled(
            String username,
            String title,
            List<String> tags,
            CourseFilter filter,
            int page,
            int limit
    ) {
        QCourse course = QCourse.course;
        QUserCourse userCourse = QUserCourse.userCourse;

        JPAQuery<Long> coursesIds = queryFactory
                .select(userCourse.course.id)
                .from(userCourse)
                .where(userCourse.user.username.eq(username));

        JPAQuery<Course> query = queryFactory
                .selectFrom(course)
                .where(course.id.notIn(coursesIds))
                .orderBy(course.title.asc());

        query = CourseQueryFilter.filtrate(query, title, tags, filter);

        return PageableBuilder.build(query, page, limit);
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

    public UserCourse findById(long userCourseId) {
        return userCourseRepository.findById(userCourseId).orElseThrow(() -> new RuntimeException("Пользовательский курс с данным id не найден"));
    }
}
