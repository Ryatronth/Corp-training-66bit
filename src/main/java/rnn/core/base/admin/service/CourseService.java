package rnn.core.base.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rnn.core.base.admin.dto.CourseDTO;
import rnn.core.base.admin.dto.mapper.CourseMapper;
import rnn.core.base.exception.AlreadyExistException;
import rnn.core.base.model.Course;
import rnn.core.base.model.repository.CourseRepository;
import rnn.core.filestorage.FileService;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final FileService fileService;

    @Transactional
    public Course create(CourseDTO courseDTO) {
        if (courseRepository.existsByTitle(courseDTO.title())) {
            throw new AlreadyExistException("Курс с названием \"%s\" уже существует.".formatted(courseDTO.title()));
        }

        Course course = courseMapper.fromDto(courseDTO);
        course.setPictureUrl(fileService.createCourseImage(UUID.randomUUID(), courseDTO.image()));

        return courseRepository.save(course);
    }

    @Transactional
    public Course update(long id, CourseDTO courseDTO) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Курс с id = %s не найден.".formatted(id)));

        courseRepository.findByTitle(courseDTO.title()).ifPresent(existedCourse -> {
            if (existedCourse.getId() != id) {
                throw new IllegalArgumentException("Курс с названием \"%s\"существует.".formatted(courseDTO.title()));
            }
        });

        course = courseMapper.updateFromDto(course, courseDTO);
        course.setPictureUrl(fileService.updateCourseImage(course, courseDTO.image()));

        return courseRepository.save(course);
    }

    @Transactional
    public void delete(long id) {
        courseRepository.findById(id).ifPresent(course -> {
            fileService.deleteCourseImage(course);
            courseRepository.delete(course);
        });
    }

    public Course find(Long id) {
        return courseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Курс с id = %s не существует.".formatted(id)));
    }

    public List<Course> findAll() {
        return courseRepository.findAll();
    }
}
