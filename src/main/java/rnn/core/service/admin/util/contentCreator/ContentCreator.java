package rnn.core.service.admin.util.contentCreator;

import rnn.core.model.admin.dto.ContentDTO;
import rnn.core.model.admin.Content;
import rnn.core.model.admin.Topic;

import java.util.List;

public interface ContentCreator {
    Content create(Topic topic, ContentDTO contentDTO);
    List<Content.Type> getType();
}
