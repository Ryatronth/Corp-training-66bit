package rnn.core.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rnn.core.model.admin.GroupDeadline;
import rnn.core.model.user.UserCourse;
import rnn.core.model.user.UserModule;
import rnn.core.model.user.dto.UserModuleDTO;
import rnn.core.model.user.repository.UserModuleRepository;
import rnn.core.service.admin.DeadlineService;
import rnn.core.service.admin.ModuleService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserModuleService {
    private final UserCourseService userCourseService;
    private final DeadlineService deadlineService;
    private final ModuleService moduleService;
    private final UserModuleRepository userModuleRepository;

    public UserModuleDTO create(long moduleId, long courseId, long groupId) {
        UserCourse course = userCourseService.find(courseId);
        GroupDeadline deadline = deadlineService.findByModuleId(moduleId, groupId);

        UserModule module = UserModule
                .builder()
                .course(course)
                .deadline(deadline)
                .build();

        module = userModuleRepository.save(module);

        return UserModuleDTO
                .builder()
                .id(module.getId())
                .currentScore(module.getCurrentScore())
                .deadline(deadline)
                .module(module.getDeadline().getModule())
                .build();
    }

    @Transactional
    public List<UserModuleDTO> findAllByCourseId(long courseId, long groupId, long userCourseId) {
        return moduleService.findAllByCourseIdWithDeadlinesAndUserModules(courseId, groupId, userCourseId);
    }
}
