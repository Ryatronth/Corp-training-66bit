package rnn.core.service.admin.util.content.processor;

import org.springframework.stereotype.Component;
import rnn.core.model.admin.Content;
import rnn.core.model.admin.Topic;
import rnn.core.model.admin.content.FreeformContent;
import rnn.core.model.admin.dto.ContentDTO;
import rnn.core.model.admin.dto.contentImpl.FreeformContentDTO;

import java.util.List;

@Component
public class FreeformProcessor<T> implements ContentProcessor<T> {
    @Override

    public Content process(T entity, ContentDTO contentDTO) {
        FreeformContentDTO freeformContentDTO = (FreeformContentDTO) contentDTO;

        FreeformContent content = FreeformContent
                .builder()
                .type(freeformContentDTO.getType())
                .description(freeformContentDTO.getDescription())
                .score(freeformContentDTO.getScore())
                .position(freeformContentDTO.getPosition())
                .title(freeformContentDTO.getTitle())
                .build();

        if (entity instanceof Topic topic) {
            content.setTopic(topic);
        }

        if (entity instanceof FreeformContent freeformContent) {
            content.setId(freeformContent.getId());
            content.setTopic(freeformContent.getTopic());
        }

        return content;
    }

    @Override
    public List<Content.Type> getType() {
        return List.of(Content.Type.FREEFORM_ANSWER);
    }
}
