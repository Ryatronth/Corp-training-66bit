package rnn.core.model.admin.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record DeadlineDTO(Long moduleId, LocalDateTime startTime, LocalDateTime endTime) {
}
