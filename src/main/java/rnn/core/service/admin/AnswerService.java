package rnn.core.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rnn.core.model.admin.Answer;
import rnn.core.model.admin.repository.AnswerRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AnswerService {
    private final AnswerRepository answerRepository;

    public Answer build(String answer) {
        return Answer.builder().answer(answer).build();
    }

    public List<Answer> buildAll(List<String> answers) {
        List<Answer> answerEntities = new ArrayList<>();
        for (String answer : answers) {
            answerEntities.add(build(answer));
        }
        return answerEntities;
    }
}
