package rnn.core.base.admin.dto;

import lombok.Builder;

@Builder
public record CourseCreationDTO(String name, String description, String authorName, int score) {
}
