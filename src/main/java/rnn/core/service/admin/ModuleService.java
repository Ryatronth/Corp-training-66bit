package rnn.core.service.admin;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rnn.core.model.admin.Course;
import rnn.core.model.admin.GroupDeadline;
import rnn.core.model.admin.Module;
import rnn.core.model.admin.dto.ModuleDTO;
import rnn.core.model.admin.dto.ModuleWithTopicsDTO;
import rnn.core.model.admin.repository.ModuleRepository;
import rnn.core.model.user.dto.UserModuleDTO;

import java.sql.Timestamp;
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
        return moduleRepository.findAllByCourseIdFetchTopic(parentId);
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

    public List<UserModuleDTO> findAllByCourseIdWithDeadlinesAndUserModules(long courseId, long groupId, long userCourseId) {
        return moduleRepository.findModulesWithDeadlinesAndUserModules(courseId, groupId, userCourseId)
                .stream()
                .map(o -> UserModuleDTO
                        .builder()
                        .id(o[7] == null ? -1 : (long) o[7])
                        .currentScore(o[8] == null ? -1 : (int) o[8])
                        .deadline(GroupDeadline
                                .builder()
                                .id(o[4] == null ? -1 : (long) o[4])
                                .startTime(o[5] == null ? null : ((Timestamp) o[5]).toLocalDateTime())
                                .endTime(o[6] == null ? null : ((Timestamp) o[6]).toLocalDateTime())
                                .build()
                        )
                        .module(Module
                                .builder()
                                .id((long) o[0])
                                .position((int) o[1])
                                .title((String) o[2])
                                .score((int) o[3])
                                .build()
                        )
                        .build())
                .toList();
    }
}
