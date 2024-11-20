package rnn.core.model.admin.dto.contentImpl;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;
import rnn.core.model.admin.dto.ContentDTO;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@ToString
public class FileContentDTO extends ContentDTO {
    private MultipartFile file;
}
