package rnn.core.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rnn.core.model.admin.Answer;
import rnn.core.model.admin.content.DetailedContent;
import rnn.core.model.admin.repository.AnswerRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AnswerService {
    private final AnswerRepository answerRepository;

    public Answer build(DetailedContent content, String answer) {
        return Answer.builder().answer(answer).content(content).build();
    }

    public List<Answer> buildAll(DetailedContent content, List<String> answers) {
        List<Answer> answerEntities = new ArrayList<>();
        for (String answer : answers) {
            answerEntities.add(build(content, answer));
        }
        return answerEntities;
    }
}
