package rnn.core.model.admin.dto;

import java.util.List;

public record GroupDTO(String name, List<Long> userIds, List<DeadlineDTO> deadlines) {
}
