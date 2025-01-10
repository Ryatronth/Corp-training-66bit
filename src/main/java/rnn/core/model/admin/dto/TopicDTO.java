package rnn.core.model.admin.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TopicDTO(
        @Min(value = 0) int position,
        @NotNull @NotBlank String title
) {
}
