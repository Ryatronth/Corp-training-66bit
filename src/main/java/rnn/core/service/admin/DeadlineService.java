package rnn.core.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rnn.core.model.admin.Group;
import rnn.core.model.admin.GroupDeadline;
import rnn.core.model.admin.dto.DeadlineDTO;
import rnn.core.model.admin.repository.GroupDeadlineRepository;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class DeadlineService {
    private final GroupDeadlineRepository groupDeadlineRepository;
    private final ModuleService moduleService;

    public GroupDeadline create(Group group, DeadlineDTO dto) {
        GroupDeadline deadline = GroupDeadline
                .builder()
                .startTime(dto.startTime())
                .endTime(dto.endTime())
                .module(moduleService.find(dto.moduleId()))
                .group(group)
                .build();
        return groupDeadlineRepository.save(deadline);
    }

    public List<GroupDeadline> createAll(Group group, List<DeadlineDTO> dtos) {
        List<GroupDeadline> deadlines = new ArrayList<>();
        for (DeadlineDTO dto : dtos) {
            GroupDeadline deadline = GroupDeadline
                    .builder()
                    .startTime(dto.startTime())
                    .endTime(dto.endTime())
                    .module(moduleService.find(dto.moduleId()))
                    .group(group)
                    .build();
            deadlines.add(deadline);
        }
        return groupDeadlineRepository.saveAll(deadlines);
    }
}
