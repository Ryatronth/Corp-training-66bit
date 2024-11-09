package rnn.core.base.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rnn.core.base.admin.dto.ModuleDTO;
import rnn.core.base.admin.dto.ModuleWithTopicsDTO;
import rnn.core.base.model.Course;
import rnn.core.base.model.Module;
import rnn.core.base.model.repository.ModuleRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ModuleService {
    private final ModuleRepository moduleRepository;
    private final CourseService courseService;

    @Transactional
    public Module create(long courseId, ModuleDTO moduleDTO) {
        Course course = courseService.find(courseId);

        Module module = Module
                .builder()
                .position(moduleDTO.position())
                .title(moduleDTO.title())
                .course(course)
                .build();

        List<Module> modules = moduleRepository.findAllWhichPositionIsHigherOrEqual(module.getCourse().getId(), module.getPosition());
        for (Module m : modules) {
            m.setPosition(m.getPosition() + 1);
        }
        moduleRepository.saveAllAndFlush(modules);

        return moduleRepository.saveAndFlush(module);
    }

    @Transactional
    public Module update(long id, ModuleDTO moduleDTO) {
        Module module = find(id);

        int delta = module.getPosition() - moduleDTO.position();

        if (delta != 0) {
            List<Module> modules = findByCourseId(module.getCourse().getId());

            for (Module m : modules) {
                if (m.getId() == module.getId()) {
                    continue;
                }

                if (delta < 0 && m.getPosition() > module.getPosition() && m.getPosition() <= moduleDTO.position()) {
                    m.setPosition(m.getPosition() - 1);
                }

                if (delta > 0 && m.getPosition() > moduleDTO.position() && m.getPosition() <= module.getPosition()) {
                    m.setPosition(m.getPosition() + 1);
                }
            }

            moduleRepository.saveAllAndFlush(modules);

            module.setPosition(module.getPosition());
        }

        module.setTitle(moduleDTO.title());
        return moduleRepository.saveAndFlush(module);
    }

    @Transactional
    public void delete(long id) {
        moduleRepository.findById(id).ifPresent(existingModule -> {
            List<Module> modules = moduleRepository.findAllWhichPositionIsHigher(existingModule.getCourse().getId(), existingModule.getPosition());

            for (Module m : modules) {
                m.setPosition(m.getPosition() - 1);
            }
            moduleRepository.delete(existingModule);

            moduleRepository.saveAll(modules);
        });
    }

    protected List<Module> findByCourseId(long courseId) {
        return moduleRepository.findAllByCourseIdFetchTopic(courseId);
    }

    public List<ModuleWithTopicsDTO> findByCourseIdWithTopics(long courseId) {
        return findByCourseId(courseId)
                .stream()
                .map(module -> ModuleWithTopicsDTO
                        .builder()
                        .id(module.getId())
                        .position(module.getPosition())
                        .score(module.getScore())
                        .title(module.getTitle())
                        .topics(module.getTopics())
                        .build()
                ).toList();
    }

    public Module find(long id) {
        return moduleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Модуль с id = %s не найден.".formatted(id)));
    }
}
