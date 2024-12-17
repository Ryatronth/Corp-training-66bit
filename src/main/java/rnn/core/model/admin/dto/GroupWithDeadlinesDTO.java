package rnn.core.model.admin.dto;

import lombok.Builder;
import rnn.core.model.admin.Group;
import rnn.core.model.admin.GroupDeadline;

import java.util.List;

@Builder
public record GroupWithDeadlinesDTO(Group group, List<GroupDeadline> deadlines) {
}
