package rnn.core.base.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import rnn.core.base.admin.dto.CourseCreationDTO;
import rnn.core.base.admin.dto.mapper.CourseMapper;
import rnn.core.base.admin.exception.CourseAlreadyExistException;
import rnn.core.base.model.Course;
import rnn.core.base.model.repository.CourseRepository;
import rnn.core.filestorage.FileStorage;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final FileStorage fileStorage;

    @Transactional
    public Course createAndSaveCourse(CourseCreationDTO courseCreationDTO, MultipartFile file) {
        Course generetedCourse = create(courseCreationDTO);
        generetedCourse.setPictureUrl(createCourseImage(file));
        return courseRepository.save(generetedCourse);
    }

    protected String createCourseImage(MultipartFile file) {
        UUID uuid = UUID.randomUUID();
        return fileStorage.uploadCourseImage(uuid, file);
    }

    protected Course create(CourseCreationDTO courseDTO) {
        boolean isNameExisted = courseRepository.existsByName(courseDTO.name());

        if (isNameExisted) {
            throw new CourseAlreadyExistException("Курс с названием \"%s\" уже существует.".formatted(courseDTO.name()));
        }

        return courseMapper.fromCreationDto(courseDTO);
    }

    public Course get(Long id) {
        return courseRepository.findById(id).orElseThrow(() -> new IllegalStateException("Курс с id = %s не существует.".formatted(id)));
    }
}
