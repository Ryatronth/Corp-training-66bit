package rnn.core.service.admin.util.contentCreator.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rnn.core.model.admin.Answer;
import rnn.core.model.admin.Question;
import rnn.core.model.admin.dto.ContentDTO;
import rnn.core.model.admin.dto.contentImpl.AnswerContentDTO;
import rnn.core.service.admin.AnswerService;
import rnn.core.service.admin.QuestionService;
import rnn.core.model.admin.Content;
import rnn.core.model.admin.Topic;
import rnn.core.model.admin.content.AnswerContent;
import rnn.core.service.admin.util.contentCreator.ContentCreator;

import java.util.List;

@RequiredArgsConstructor
@Component
public class AnswerCreator implements ContentCreator {
    private final QuestionService questionService;
    private final AnswerService answerService;

    @Override
    public Content create(Topic topic, ContentDTO contentDTO) {
        AnswerContentDTO answerContentDTO = (AnswerContentDTO) contentDTO;

        AnswerContent content = AnswerContent
                .builder()
                .title(answerContentDTO.getTitle())
                .position(answerContentDTO.getPosition())
                .score(answerContentDTO.getScore())
                .type(answerContentDTO.getType())
                .countAttempts(answerContentDTO.getCountAttempts())
                .topic(topic)
                .build();

        List<Question> questions = questionService.buildAll(content, answerContentDTO.getQuestions());
        content.setQuestions(questions);

        List<Answer> answers = answerService.buildAll(content, answerContentDTO.getAnswers());
        content.setAnswers(answers);

        return content;
    }

    @Override
    public List<Content.Type> getType() {
        return List.of(Content.Type.SINGLE_ANSWER, Content.Type.MULTI_ANSWER);
    }
}
