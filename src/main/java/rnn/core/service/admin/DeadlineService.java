package rnn.core.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rnn.core.model.admin.Group;
import rnn.core.model.admin.GroupDeadline;
import rnn.core.model.admin.dto.DeadlineDTO;

@RequiredArgsConstructor
@Service
public class DeadlineService {
    private final ModuleService moduleService;

    public GroupDeadline create(Group group, DeadlineDTO dto) {
        return GroupDeadline
                .builder()
                .endTime(dto.endTime())
                .startTime(dto.startTime())
                .module(moduleService.find(dto.moduleId()))
                .group(group)
                .build();
    }
}
