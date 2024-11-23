package rnn.core.model.admin.dto;

import java.time.LocalDateTime;

public record DeadlineDTO(long moduleId, LocalDateTime startTime, LocalDateTime endTime) {
}
