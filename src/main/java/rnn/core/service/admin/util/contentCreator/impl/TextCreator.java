package rnn.core.service.admin.util.contentCreator.impl;

import org.springframework.stereotype.Component;
import rnn.core.model.admin.dto.ContentDTO;
import rnn.core.model.admin.dto.contentImpl.TextContentDTO;
import rnn.core.model.admin.Content;
import rnn.core.model.admin.content.TextContent;
import rnn.core.model.admin.Topic;
import rnn.core.service.admin.util.contentCreator.ContentCreator;

import java.util.List;


@Component
public class TextCreator implements ContentCreator {
    @Override
    public Content create(Topic topic, ContentDTO contentDTO) {
        TextContentDTO textContentDTO = (TextContentDTO) contentDTO;
        return TextContent
                .builder()
                .title(textContentDTO.getTitle())
                .description(textContentDTO.getDescription())
                .type(contentDTO.getType())
                .position(contentDTO.getPosition())
                .topic(topic)
                .build();
    }

    @Override
    public List<Content.Type> getType() {
        return List.of(Content.Type.TEXT);
    }
}
