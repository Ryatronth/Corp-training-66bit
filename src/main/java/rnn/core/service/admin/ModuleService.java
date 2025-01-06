package rnn.core.service.admin;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rnn.core.model.admin.dto.ModuleDTO;
import rnn.core.model.admin.dto.ModuleWithTopicsDTO;
import rnn.core.model.admin.Course;
import rnn.core.model.admin.Module;
import rnn.core.model.admin.repository.ModuleRepository;

import java.util.ArrayList;
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
        return moduleRepository.findAllByCourseId(parentId);
    }

    @Transactional
    public List<Module> create(long courseId, List<ModuleDTO> dtos) {
        List<Module> modules = new ArrayList<>(dtos.size());
        for (ModuleDTO dto : dtos) {
            modules.add(create(courseId, dto));
        }
        return modules;
    }

    @Transactional
    public Module create(long courseId, ModuleDTO moduleDTO) {
        Course course = courseService.find(courseId);
        course.setCountModules(course.getCountModules() + 1);

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
        Module module = findWithCourse(id);

        Course course = module.getCourse();
        course.setScore(course.getScore() - module.getScore());

        super.delete(module, course.getId());
    }

    public List<ModuleWithTopicsDTO> findByCourseIdWithTopics(long courseId) {
        return moduleRepository.findAllByCourseIdFetchTopic(courseId).stream()
                .map(o ->
                        ModuleWithTopicsDTO
                                .builder()
                                .id(o.getId())
                                .title(o.getTitle())
                                .score(o.getScore())
                                .topics(o.getTopics())
                                .build()
                )
                .toList();
    }

    public List<Module> findByCourseIdFetchTopics(long courseId) {
        return moduleRepository.findAllByCourseIdFetchTopic(courseId);
    }

    public Module find(long id) {
        return moduleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Модуль с id = %s не найден.".formatted(id)));
    }

    public Module findWithCourse(long id) {
        return moduleRepository.findByIdFetchCourse(id).orElseThrow(() -> new IllegalArgumentException("Модуль с id = %s не найден.".formatted(id)));
    }
}
