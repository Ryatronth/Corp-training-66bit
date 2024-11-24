package rnn.core.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rnn.core.model.admin.dto.CourseDTO;
import rnn.core.model.admin.Course;
import rnn.core.model.admin.converter.TagConverter;
import rnn.core.model.admin.repository.CourseRepository;
import rnn.core.service.filestorage.FileService;
import rnn.core.service.security.UserService;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final UserService userService;
    private final TagConverter tagConverter;
    private final FileService fileService;

    @Transactional()
    public Course create(CourseDTO courseDTO) {
        Course course = Course
                .builder()
                .author(userService.findOne(courseDTO.authorName()))
                .tags(tagConverter.convertToEntityAttribute(courseDTO.tags()))
                .title(courseDTO.title())
                .description(courseDTO.description())
                .pictureUrl(fileService.createCourseImage(UUID.randomUUID(), courseDTO.image()))
                .build();
        return courseRepository.save(course);
    }

    @Transactional
    public Course update(long id, CourseDTO courseDTO) {
        Course course = find(id);
        course.setTags(tagConverter.convertToEntityAttribute(courseDTO.tags()));
        course.setTitle(courseDTO.title());
        course.setDescription(courseDTO.description());
        course.setPictureUrl(fileService.updateCourseImage(course, courseDTO.image()));
        return courseRepository.save(course);
    }

    @Transactional
    public void delete(long id) {
        courseRepository.deleteById(id);
    }

    public Course find(long id) {
        return courseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Курс с id = %s не найден.".formatted(id)));
    }

    public Page<Course> findAll(int page, int limit) {
        return courseRepository.findCoursesWithAuthor(PageRequest.of(page, limit));
    }

    public List<Course> findAllNotEnrolledByUser(String username) {
        return courseRepository.findCoursesNotEnrolledByUser(username);
    }
}
