package rnn.core.model.admin.dto;

import java.util.List;

public record GroupDTO(String name, List<Long> ids, List<DeadlineDTO> deadlines) {
}
