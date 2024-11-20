package rnn.core.service.admin.util.content.processor;

import rnn.core.model.admin.dto.ContentDTO;
import rnn.core.model.admin.Content;

import java.util.List;

public interface ContentProcessor<T> {
    Content process(T entity, ContentDTO contentDTO);

    List<Content.Type> getType();
}
