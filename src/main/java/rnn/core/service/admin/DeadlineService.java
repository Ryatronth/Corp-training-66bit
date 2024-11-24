package rnn.core.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rnn.core.model.admin.Group;
import rnn.core.model.admin.GroupDeadline;
import rnn.core.model.admin.dto.DeadlineDTO;
import rnn.core.model.admin.repository.GroupDeadlineRepository;

@RequiredArgsConstructor
@Service
public class DeadlineService {
    private final ModuleService moduleService;
    private final GroupDeadlineRepository groupDeadlineRepository;

    public GroupDeadline create(Group group, DeadlineDTO dto) {
        return GroupDeadline
                .builder()
                .endTime(dto.endTime())
                .startTime(dto.startTime())
                .module(moduleService.find(dto.moduleId()))
                .group(group)
                .build();
    }

    public GroupDeadline findByModuleId(long moduleId, long groupId) {
        return groupDeadlineRepository.findByModuleIdAndGroupId(moduleId, groupId).orElseThrow(() -> new IllegalArgumentException("Дедлайн для модуля с id = %s не найден".formatted(moduleId)));
    }
}
