package rnn.core.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rnn.core.model.admin.Question;
import rnn.core.model.admin.content.AnswerContent;
import rnn.core.model.admin.repository.QuestionRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class QuestionService {
    private final QuestionRepository questionRepository;

    public Question build(AnswerContent content, String question) {
        return Question.builder().question(question).content(content).build();
    }

    public List<Question> buildAll(AnswerContent content, List<String> questions) {
        List<Question> questionEntities = new ArrayList<>();
        for (String question : questions) {
            questionEntities.add(build(content, question));
        }
        return questionEntities;
    }
}
