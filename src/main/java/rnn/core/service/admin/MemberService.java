package rnn.core.service.admin;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import rnn.core.model.admin.QGroup;
import rnn.core.model.admin.dto.UserCourseGroupDTO;
import rnn.core.model.admin.dto.UserGroupDTO;
import rnn.core.model.querydsl.PageableBuilder;
import rnn.core.model.security.QUser;
import rnn.core.model.security.User;
import rnn.core.model.user.QUserCourse;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final JPAQueryFactory queryFactory;

    public Page<User> findAllWithoutCourse(long courseId, String name, int page, int limit) {
        QUser user = QUser.user;
        QUserCourse userCourse = QUserCourse.userCourse;

        JPQLQuery<Integer> usersInCourseSubquery = JPAExpressions
                .selectOne()
                .from(userCourse)
                .where(
                        userCourse.course.id.eq(courseId).and(userCourse.user.username.eq(user.username))
                );

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(usersInCourseSubquery.notExists());

        if (name != null && !name.trim().isEmpty()) {
            builder.and(user.username.containsIgnoreCase(name.trim()).or(user.email.containsIgnoreCase(name.trim())));
        }

        JPAQuery<User> query = queryFactory
                .selectDistinct(user)
                .from(user)
                .leftJoin(user.role).fetchJoin()
                .leftJoin(user.userCourses, userCourse)
                .where(builder)
                .orderBy(user.username.asc());

        return PageableBuilder.build(query, page, limit);
    }

    public Page<User> findAllWithoutCourseOrInDefault(long courseId, String name, int page, int limit) {
        QUser user = QUser.user;
        QUserCourse userCourse = QUserCourse.userCourse;
        QGroup group = QGroup.group;

        JPQLQuery<Integer> usersInCourseSubquery = JPAExpressions
                .selectOne()
                .from(group)
                .join(group.users, user)
                .where(
                        group.course.id.eq(courseId)
                                .and(user.username.eq(user.username))
                                .and(group.isDefault.isFalse())
                );

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(usersInCourseSubquery.notExists());
        builder.or(userCourse.course.id.isNull());

        if (name != null && !name.trim().isEmpty()) {
            builder.and(user.username.containsIgnoreCase(name.trim()).or(user.email.containsIgnoreCase(name.trim())));
        }

        JPAQuery<User> query = queryFactory
                .selectDistinct(user)
                .from(user)
                .leftJoin(user.role).fetchJoin()
                .leftJoin(user.userCourses, userCourse)
                .where(builder)
                .orderBy(user.username.asc());

        return PageableBuilder.build(query, page, limit);
    }

    public Page<UserGroupDTO> findAllWithoutCourseOrInGroupOrInDefault(long courseId, long groupId, String name, int page, int limit) {
        QUser user = QUser.user;
        QUserCourse userCourse = QUserCourse.userCourse;
        QGroup group = QGroup.group;

        Expression<Boolean> inGroupExpression = Expressions.booleanTemplate(
                "case when {0} = {1} then true else false end", group.id, groupId
        );

        JPQLQuery<Integer> usersInCourseSubquery = JPAExpressions
                .selectOne()
                .from(userCourse)
                .where(
                        userCourse.course.id.eq(courseId)
                                .and(userCourse.user.username.eq(user.username))
                );

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(userCourse.course.id.eq(courseId).and(group.id.eq(groupId)));
        builder.or(group.course.id.eq(courseId).and(group.isDefault.isTrue()));
        builder.or(usersInCourseSubquery.notExists());
        builder.or(userCourse.id.isNull());

        if (name != null && !name.trim().isEmpty()) {
            builder.and(user.username.containsIgnoreCase(name.trim()).or(user.email.containsIgnoreCase(name.trim())));
        }

        JPAQuery<UserGroupDTO> query = queryFactory
                .selectDistinct(Projections.constructor(UserGroupDTO.class, user, inGroupExpression))
                .from(user)
                .join(user.role).fetchJoin()
                .join(user.groups, group)
                .leftJoin(user.userCourses, userCourse)
                .where(builder)
                .orderBy(user.username.asc());

        return PageableBuilder.build(query, page, limit);
    }

    public Page<UserCourseGroupDTO> findAllWithCourseAndGroup(
            long courseId,
            String name,
            String sort,
            String direction,
            int page,
            int limit
    ) {
        QUser user = QUser.user;
        QGroup group = QGroup.group;
        QUserCourse userCourse = QUserCourse.userCourse;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(userCourse.course.id.eq(courseId));
        builder.and(group.course.id.eq(courseId));

        if (name != null && !name.trim().isEmpty()) {
            builder.and(user.username.containsIgnoreCase(name.trim()).or(user.email.containsIgnoreCase(name.trim())));
        }

        JPAQuery<UserCourseGroupDTO> query = queryFactory
                .selectDistinct(Projections.constructor(
                        UserCourseGroupDTO.class,
                        user,
                        userCourse.isCompleted,
                        group,
                        userCourse.currentScore
                ))
                .from(user)
                .leftJoin(user.role).fetchJoin()
                .leftJoin(user.userCourses, userCourse)
                .leftJoin(user.groups, group)
                .where(builder);

        if (sort != null && !sort.trim().isEmpty() && direction != null && !direction.trim().isEmpty()) {
            query.orderBy(createOrderSpecifier(sort, direction));
        }

        return PageableBuilder.build(query, page, limit);
    }

    private OrderSpecifier<?> createOrderSpecifier(String sort, String direction) {
        QUser user = QUser.user;
        QUserCourse userCourse = QUserCourse.userCourse;
        QGroup group = QGroup.group;

        Order order = direction.equalsIgnoreCase("asc") ? Order.ASC : Order.DESC;
        return switch (sort) {
            case "username" -> new OrderSpecifier<>(
                    order,
                    user.username
            );
            case "group" -> new OrderSpecifier<>(
                    order,
                    group.name
            );
            case "progress" -> new OrderSpecifier<>(
                    order,
                    userCourse.currentScore
            );
            case "status" -> new OrderSpecifier<>(
                    order,
                    userCourse.isCompleted
            );
            default -> throw new IllegalArgumentException("Invalid sort field: " + sort);
        };
    }
}
