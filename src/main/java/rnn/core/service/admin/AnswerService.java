package rnn.core.service.admin;

import org.springframework.stereotype.Service;
import rnn.core.model.admin.Answer;
import rnn.core.model.admin.content.DetailedContent;
import rnn.core.model.admin.dto.contentImpl.DetailedContentDTO;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnswerService {
    public Answer build(DetailedContent content, DetailedContentDTO.AnswerDTO answer) {
        return Answer.builder().answer(answer.answer()).isRight(answer.isRight()).content(content).build();
    }

    public List<Answer> buildAll(DetailedContent content, List<DetailedContentDTO.AnswerDTO> answers) {
        List<Answer> answerEntities = new ArrayList<>();
        for (DetailedContentDTO.AnswerDTO answer : answers) {
            answerEntities.add(build(content, answer));
        }
        return answerEntities;
    }
}
