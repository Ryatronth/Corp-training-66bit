package rnn.core.base.admin.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CourseDTO(String name, String description, String pictureUrl, String authorName, int score, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
