package rnn.core.service.admin.util.contentCreator.impl;

import org.springframework.stereotype.Component;
import rnn.core.model.admin.dto.ContentDTO;
import rnn.core.model.admin.dto.contentImpl.FreeformContentDTO;
import rnn.core.model.admin.Content;
import rnn.core.model.admin.Topic;
import rnn.core.model.admin.content.FreeformContent;
import rnn.core.service.admin.util.contentCreator.ContentCreator;

import java.util.List;

@Component
public class FreeformCreator implements ContentCreator {
    @Override
    public Content create(Topic topic, ContentDTO contentDTO) {
        FreeformContentDTO freeformContentDTO = (FreeformContentDTO) contentDTO;
        return FreeformContent
                .builder()
                .topic(topic)
                .type(freeformContentDTO.getType())
                .score(freeformContentDTO.getScore())
                .position(freeformContentDTO.getPosition())
                .title(freeformContentDTO.getTitle())
                .build();
    }

    @Override
    public List<Content.Type> getType() {
        return List.of(Content.Type.FREEFORM_ANSWER);
    }
}
