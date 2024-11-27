package rnn.core.service.admin.util.content.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rnn.core.model.admin.Answer;
import rnn.core.model.admin.Content;
import rnn.core.model.admin.Topic;
import rnn.core.model.admin.content.DetailedContent;
import rnn.core.model.admin.dto.ContentDTO;
import rnn.core.model.admin.dto.contentImpl.DetailedContentDTO;
import rnn.core.service.admin.AnswerService;

import java.util.List;

@RequiredArgsConstructor
@Component
public class DetailedProcessor<T> implements ContentProcessor<T> {
    protected final AnswerService answerService;

    @Override
    public Content process(T entity, ContentDTO contentDTO) {
        DetailedContentDTO detailedContentDTO = (DetailedContentDTO) contentDTO;

        DetailedContent content = DetailedContent
                .builder()
                .title(detailedContentDTO.getTitle())
                .description(detailedContentDTO.getDescription())
                .position(detailedContentDTO.getPosition())
                .score(detailedContentDTO.getScore())
                .type(detailedContentDTO.getType())
                .countAttempts(detailedContentDTO.getCountAttempts())
                .build();

        if (entity instanceof Topic topic) {
            content.setTopic(topic);
        }

        if (entity instanceof DetailedContent detailedContent) {
            content.setId(detailedContent.getId());
            content.setTopic(detailedContent.getTopic());
        }

        List<Answer> answers = answerService.buildAll(content, detailedContentDTO.getAnswers());
        content.setAnswers(answers);

        return content;
    }

    @Override
    public List<Content.Type> getType() {
        return List.of(Content.Type.DETAILED_ANSWER);
    }
}
