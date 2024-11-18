package rnn.core.service.admin.util.contentCreator.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
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

        return AnswerContent
                .builder()
                .title(answerContentDTO.getTitle())
                .position(answerContentDTO.getPosition())
                .score(answerContentDTO.getScore())
                .type(answerContentDTO.getType())
                .countAttempts(answerContentDTO.getCountAttempts())
                .questions(questionService.buildAll(answerContentDTO.getQuestions()))
                .answers(answerService.buildAll(answerContentDTO.getAnswers()))
                .topic(topic)
                .build();
    }

    @Override
    public List<Content.Type> getType() {
        return List.of(Content.Type.SINGLE_ANSWER, Content.Type.MULTI_ANSWER);
    }
}
