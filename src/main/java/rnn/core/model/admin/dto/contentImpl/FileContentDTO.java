package rnn.core.model.admin.dto.contentImpl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import rnn.core.model.admin.dto.ContentDTO;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
public class FileContentDTO extends ContentDTO {
    private String fileUrl;
}
