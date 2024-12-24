package rnn.core.service.admin;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import rnn.core.controller.admin.filter.CourseFilter;
import rnn.core.event.event.CreateCourseEvent;
import rnn.core.model.admin.Course;
import rnn.core.model.admin.QCourse;
import rnn.core.model.admin.converter.TagConverter;
import rnn.core.model.admin.dto.CourseWithImageDTO;
import rnn.core.model.admin.dto.CourseWithoutImageDTO;
import rnn.core.model.admin.repository.CourseRepository;
import rnn.core.model.querydsl.PageableBuilder;
import rnn.core.model.security.QRole;
import rnn.core.model.security.QUser;
import rnn.core.service.filestorage.FileService;
import rnn.core.service.security.UserService;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CourseService {
    private final ApplicationEventPublisher eventPublisher;
    private final JPAQueryFactory queryFactory;

    private final CourseRepository courseRepository;
    private final UserService userService;
    private final TagConverter tagConverter;
    private final FileService fileService;

    @Transactional
    public Course create(CourseWithImageDTO courseDTO) {
        Course course = Course
                .builder()
                .author(userService.findOne(courseDTO.authorName()))
                .tags(tagConverter.convertToEntityAttribute(courseDTO.tags()))
                .title(courseDTO.title())
                .description(courseDTO.description())
                .pictureUrl(fileService.createCourseImage(UUID.randomUUID(), courseDTO.image()))
                .build();

        course = courseRepository.save(course);
        eventPublisher.publishEvent(new CreateCourseEvent(this, course));
        return course;
    }

    @Transactional
    public Course updateFields(long id, CourseWithoutImageDTO courseDTO) {
        Course course = find(id);
        course.setTags(tagConverter.convertToEntityAttribute(courseDTO.tags()));
        course.setTitle(courseDTO.title());
        course.setDescription(courseDTO.description());
        return courseRepository.save(course);
    }

    @Transactional
    public Course updateImage(long id, MultipartFile image) {
        Course course = find(id);
        course.setPictureUrl(fileService.updateCourseImage(course, image));
        return courseRepository.save(course);
    }

    @Transactional
    public void delete(long id) {
        courseRepository.deleteById(id);
    }

    public Course find(long id) {
        return courseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Курс с id = %s не найден.".formatted(id)));
    }

    public Page<Course> findAll(String title, List<String> tags, CourseFilter filter, int page, int limit) {
        QCourse course = QCourse.course;
        QUser user = QUser.user;
        QRole role = QRole.role;

        BooleanBuilder builder = new BooleanBuilder();

        if (title != null && !title.trim().isEmpty()) {
            builder.and(course.title.containsIgnoreCase(title.trim()));
        }

        if (tags != null && !tags.isEmpty()) {
            StringBuilder template = new StringBuilder();

            for (int i = 0; i < tags.size(); i++) {
                template.append("lower({0}) like '%\"name\":\"%").append(tags.get(i).trim().toLowerCase()).append("%\",\"color%'");
                if (i < tags.size() - 1) {
                    template.append(" or ");
                }
            }

            System.out.println(template);

            builder.and(Expressions.booleanTemplate(
                    template.toString(),
                    course.tags
            ));
        }

        switch (filter) {
            case PUBLISHED -> builder.and(course.isPublished.eq(true));
            case UNPUBLISHED -> builder.and(course.isPublished.eq(false));
            case ALL -> {
            }
        }

        JPAQuery<Course> query = queryFactory
                .selectFrom(course)
                .leftJoin(course.author, user).fetchJoin()
                .leftJoin(user.role, role).fetchJoin()
                .where(builder)
                .orderBy(course.title.asc());

        return PageableBuilder.build(query, page, limit);
    }

    public Course publish(long courseId) {
        Course course = find(courseId);
        course.setPublished(true);
        return courseRepository.save(course);
    }

    public Course unpublish(long courseId) {
        Course course = find(courseId);
        course.setPublished(false);
        return courseRepository.save(course);
    }
}
