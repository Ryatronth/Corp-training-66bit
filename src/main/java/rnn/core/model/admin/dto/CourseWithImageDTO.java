package rnn.core.model.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;
import rnn.core.validation.annotations.FileNotEmpty;
import rnn.core.validation.annotations.IsImage;

@Builder
public record CourseWithImageDTO(
        @NotNull @NotBlank String title,
        @NotNull @NotBlank String description,
        @NotNull @NotBlank String tags,
        @NotNull @FileNotEmpty @IsImage MultipartFile image) {
}
