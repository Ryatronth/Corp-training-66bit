package rnn.core.base.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rnn.core.base.admin.dto.ModuleDTO;
import rnn.core.base.exception.AlreadyExistException;
import rnn.core.base.model.Course;
import rnn.core.base.model.Module;
import rnn.core.base.model.repository.ModuleRepository;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ModuleService {
    private final ModuleRepository moduleRepository;
    private final CourseService courseService;

    public Module create(Long courseId, ModuleDTO moduleDTO) {
        Course course = courseService.find(courseId);

        moduleRepository.findByTitleAndCourseId(moduleDTO.title(), course.getId()).ifPresent(ignored -> {
            throw new AlreadyExistException("Модуль с именем \"%s\" уже существует.".formatted(moduleDTO.title()));
        });

        Module module = Module
                .builder()
                .position(moduleDTO.position())
                .title(moduleDTO.title())
                .course(course)
                .build();

        return moduleRepository.save(module);
    }

    public Module update(Long id, ModuleDTO moduleDTO) {
        Module module = moduleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Модуль с id = %s не найден.".formatted(id)));

        moduleRepository.findByTitleAndCourseId(moduleDTO.title(), module.getId()).ifPresent(existingModule -> {
            if (existingModule.getId() != id) {
                throw new AlreadyExistException("Модуль с именем \"%s\" уже существует.".formatted(moduleDTO.title()));
            }
        });

        module.setPosition(moduleDTO.position());
        module.setTitle(moduleDTO.title());

        return moduleRepository.save(module);
    }

    public void delete(Long id) {
        moduleRepository.findById(id).ifPresent(existingModule -> {
            List<Module> modules = moduleRepository.findAllWhichPositionIsHigher(existingModule.getCourse().getId(), existingModule.getPosition());
            moduleRepository.delete(existingModule);

            for (Module module : modules) {
                module.setPosition(module.getPosition() - 1);
            }

            moduleRepository.saveAll(modules);
        });
    }

    public List<Module> findByCourseId(Long courseId) {
        List<Module> modules = moduleRepository.findByCourseId(courseId);
        modules.sort(Comparator.comparingInt(Module::getPosition));
        return modules;
    }
}
