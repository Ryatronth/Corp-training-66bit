package rnn.core.model.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CourseWithoutImageDTO(
        @NotNull @NotBlank String title,
        @NotNull @NotBlank String description,
        @NotNull @NotBlank String tags) {
}
