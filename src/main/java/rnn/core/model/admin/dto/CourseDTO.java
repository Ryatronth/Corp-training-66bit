package rnn.core.model.admin.dto;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record CourseDTO(String title, String description, String authorName, String tags, MultipartFile image) {
}
