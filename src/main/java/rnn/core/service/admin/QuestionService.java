package rnn.core.service.admin;

import org.springframework.stereotype.Service;
import rnn.core.model.admin.Question;
import rnn.core.model.admin.content.AnswerContent;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
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
