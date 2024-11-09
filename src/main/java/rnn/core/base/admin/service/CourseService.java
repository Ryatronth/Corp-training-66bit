package rnn.core.base.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rnn.core.base.admin.dto.CourseDTO;
import rnn.core.base.admin.dto.mapper.CourseMapper;
import rnn.core.base.model.Course;
import rnn.core.base.model.repository.CourseRepository;
import rnn.core.filestorage.FileService;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final FileService fileService;

    // TODO Добавить onrollback удаление картинки
    @Transactional
    public Course create(CourseDTO courseDTO) {
        Course course = courseMapper.fromDto(courseDTO);
        course.setPictureUrl(fileService.createCourseImage(UUID.randomUUID(), courseDTO.image()));
        return courseRepository.saveAndFlush(course);
    }

    // TODO Добавить onrollback удаление картинки
    @Transactional
    public Course update(long id, CourseDTO courseDTO) {
        Course course = find(id);
        course = courseMapper.updateFromDto(course, courseDTO);
        course.setPictureUrl(fileService.updateCourseImage(course, courseDTO.image()));
        return courseRepository.saveAndFlush(course);
    }

    @Transactional
    public void delete(long id) {
        courseRepository.findById(id).ifPresent(course -> {
            fileService.deleteCourseImage(course);
            courseRepository.deleteById(id);
        });
    }

    public Course find(long id) {
        return courseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Курс с id = %s не найден.".formatted(id)));
    }

    public Page<Course> findAll(int page, int limit) {
        return courseRepository.findAll(PageRequest.of(page, limit));
    }
}
