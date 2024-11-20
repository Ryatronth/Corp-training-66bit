package rnn.core.service.admin.util.contentCreator.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rnn.core.model.admin.Answer;
import rnn.core.model.admin.dto.ContentDTO;
import rnn.core.model.admin.dto.contentImpl.DetailedContentDTO;
import rnn.core.service.admin.AnswerService;
import rnn.core.model.admin.Content;
import rnn.core.model.admin.Topic;
import rnn.core.model.admin.content.DetailedContent;
import rnn.core.service.admin.util.contentCreator.ContentCreator;

import java.util.List;

@RequiredArgsConstructor
@Component
public class DetailedCreator implements ContentCreator {
    protected final AnswerService answerService;

    @Override
    public Content create(Topic topic, ContentDTO contentDTO) {
        DetailedContentDTO detailedContentDTO = (DetailedContentDTO) contentDTO;

        DetailedContent content =  DetailedContent
                .builder()
                .title(detailedContentDTO.getTitle())
                .position(detailedContentDTO.getPosition())
                .score(detailedContentDTO.getScore())
                .type(detailedContentDTO.getType())
                .countAttempts(detailedContentDTO.getCountAttempts())
                .topic(topic)
                .build();

        List<Answer> answers = answerService.buildAll(content, detailedContentDTO.getAnswers());
        content.setAnswers(answers);

        return content;
    }

    @Override
    public List<Content.Type> getType() {
        return List.of(Content.Type.DETAILED_ANSWER);
    }
}
