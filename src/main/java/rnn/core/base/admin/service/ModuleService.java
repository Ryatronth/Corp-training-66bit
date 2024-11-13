package rnn.core.base.admin.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rnn.core.base.admin.dto.ModuleDTO;
import rnn.core.base.admin.dto.ModuleWithTopicsDTO;
import rnn.core.base.model.Course;
import rnn.core.base.model.Module;
import rnn.core.base.model.repository.ModuleRepository;

import java.util.List;

@Service
public class ModuleService extends PositionableService<Module, Long> {
    private final ModuleRepository moduleRepository;
    private final CourseService courseService;

    public ModuleService(ModuleRepository repository, CourseService courseService) {
        super(repository);
        this.moduleRepository = repository;
        this.courseService = courseService;
    }

    @Override
    protected int getPosition(Module entity) {
        return entity.getPosition();
    }

    @Override
    protected void setPosition(Module entity, int position) {
        entity.setPosition(position);
    }

    @Override
    protected List<Module> findAllHigherOrEqualPosition(long parentId, int position) {
        return moduleRepository.findAllWhichPositionIsHigherOrEqual(parentId, position);
    }

    @Override
    protected List<Module> findAllHigherPosition(long parentId, int position) {
        return moduleRepository.findAllWhichPositionIsHigher(parentId, position);
    }

    @Override
    protected List<Module> findAllByParentId(long parentId) {
        return moduleRepository.findAllByCourseIdFetchTopic(parentId);
    }

    @Transactional
    public Module create(long courseId, ModuleDTO moduleDTO) {
        Course course = courseService.find(courseId);

        Module module = Module
                .builder()
                .title(moduleDTO.title())
                .course(course)
                .build();

        return super.create(module, courseId, moduleDTO.position());
    }

    @Transactional
    public Module update(long id, ModuleDTO moduleDTO) {
        Module module = find(id);

        module.setTitle(moduleDTO.title());
        return super.update(module, moduleDTO.position(), module.getCourse().getId());
    }

    @Transactional
    public void delete(long id) {
        moduleRepository.findById(id).ifPresent(existingModule -> super.delete(existingModule, existingModule.getCourse().getId()));
    }

    public List<Module> findByCourseId(long courseId) {
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
