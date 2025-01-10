package rnn.core.service.admin.util.content.processor;

import org.springframework.stereotype.Component;
import rnn.core.model.admin.Content;
import rnn.core.model.admin.Topic;
import rnn.core.model.admin.content.TextContent;
import rnn.core.model.admin.dto.ContentDTO;
import rnn.core.model.admin.dto.contentImpl.TextContentDTO;

import java.util.List;


@Component
public class TextProcessor<T> implements ContentProcessor<T> {

    @Override
    public Content process(T entity, ContentDTO contentDTO) {
        TextContentDTO textContentDTO = (TextContentDTO) contentDTO;
        TextContent content = TextContent
                .builder()
                .title(textContentDTO.getTitle().trim())
                .description(textContentDTO.getDescription().trim())
                .type(contentDTO.getType())
                .position(contentDTO.getPosition())
                .build();

        if (entity instanceof Topic topic) {
            content.setTopic(topic);
        }

        if (entity instanceof TextContent textContent) {
            content.setId(textContent.getId());
            content.setTopic(textContent.getTopic());
        }

        return content;
    }

    @Override
    public List<Content.Type> getType() {
        return List.of(Content.Type.TEXT);
    }
}
