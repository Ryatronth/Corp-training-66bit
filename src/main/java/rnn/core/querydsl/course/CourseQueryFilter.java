package rnn.core.querydsl.course;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import rnn.core.util.CourseFilter;
import rnn.core.model.admin.QCourse;
import rnn.core.querydsl.PredicateBuilder;

import java.util.List;

public class CourseQueryFilter {
    public static <T> JPAQuery<T> filtrate(
            JPAQuery<T> query,
            String title,
            List<String> tags,
            CourseFilter filter
    ) {
        QCourse course = QCourse.course;
        BooleanBuilder predicate = PredicateBuilder
                .builder()
                .and(title, course.title::containsIgnoreCase)
                .build();

        switch (filter) {
            case PUBLISHED -> predicate.and(course.isPublished.eq(true));
            case UNPUBLISHED -> predicate.and(course.isPublished.eq(false));
            case ALL -> {
            }
        }

        if (tags != null && !tags.isEmpty()) {
            StringBuilder template = new StringBuilder();
            template.append('(');
            for (int i = 0; i < tags.size(); i++) {
                template.append("lower({0}) like '%\"name\":\"%").append(tags.get(i).trim().toLowerCase()).append("%\",\"color%'");
                if (i < tags.size() - 1) {
                    template.append(" or ");
                }
            }
            template.append(')');
            predicate.and(Expressions.booleanTemplate(
                    template.toString(),
                    course.tags
            ));
        }

        return query.where(predicate);
    }
}
