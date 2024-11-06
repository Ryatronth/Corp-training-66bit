package rnn.core.base.admin.dto;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record CourseCreationDTO(String title, String description, String authorName, String tags, MultipartFile image) {
}
