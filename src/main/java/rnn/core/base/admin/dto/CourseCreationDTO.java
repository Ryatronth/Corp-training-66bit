package rnn.core.base.admin.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record CourseCreationDTO(String title, String description, String authorName, int score, List<String> tags) {
}
