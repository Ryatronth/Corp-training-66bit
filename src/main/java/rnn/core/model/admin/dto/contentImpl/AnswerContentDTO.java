package rnn.core.model.admin.dto.contentImpl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import rnn.core.model.admin.dto.ContentDTO;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
public class AnswerContentDTO extends ContentDTO {
    private int countAttempts;

    private int score;

    private List<String> questions;

    private List<String> answers;
}
