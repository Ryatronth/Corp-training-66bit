package rnn.core.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rnn.core.model.admin.Module;
import rnn.core.model.user.UserModule;
import rnn.core.model.user.dto.UserModuleWithModuleDTO;
import rnn.core.model.user.repository.UserModuleRepository;
import rnn.core.service.admin.ModuleService;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserModuleService {
    private final UserModuleRepository userModuleRepository;
    private final ModuleService moduleService;
    private final UserCourseService userCourseService;

    public UserModule create(long userCourseId, long moduleId) {
        UserModule userModule = UserModule
                .builder()
                .module(moduleService.find(moduleId))
                .course(userCourseService.findById(userCourseId))
                .topics(new ArrayList<>())
                .currentScore(0)
                .build();
        return userModuleRepository.save(userModule);
    }

    public List<UserModuleWithModuleDTO> findAllWithModule(long courseId, long userCourseId) {
        List<Module> modules = moduleService.findByCourseIdFetchTopics(courseId);
        List<UserModule> userModules = userModuleRepository.findAllByCourseIdFetchTopics(userCourseId);

        List<UserModuleWithModuleDTO> userModuleWithModuleDTOs = new ArrayList<>();
        for (Module module : modules) {
            int l = 0;
            int r = userModules.size() - 1;

            boolean found = false;
            while (l <= r) {
                int mid = l + (r - l) / 2;
                UserModule userModule = userModules.get(mid);

                if (userModule.getModule().getId() == module.getId()) {
                    userModuleWithModuleDTOs.add(
                            UserModuleWithModuleDTO
                                    .builder()
                                    .module(module)
                                    .topics(module.getTopics())
                                    .userModule(userModule)
                                    .userTopics(userModule.getTopics())
                                    .build());
                    found = true;
                    break;
                } else if (userModule.getModule().getId() > module.getId()) {
                    r--;
                } else {
                    l++;
                }
            }

            if (!found) {
                userModuleWithModuleDTOs.add(
                        UserModuleWithModuleDTO
                                .builder()
                                .module(module)
                                .topics(module.getTopics())
                                .build()
                );
            }
        }

        return userModuleWithModuleDTOs;
    }

    public UserModule find(long id) {
        return userModuleRepository.findById(id).orElseThrow(() -> new RuntimeException("Модуль пользователя с указанным id не найден"));
    }
}
