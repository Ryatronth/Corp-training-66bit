package rnn.core.service.admin.util.contentCreator.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rnn.core.model.admin.dto.ContentDTO;
import rnn.core.model.admin.dto.contentImpl.FileContentDTO;
import rnn.core.model.admin.Content;
import rnn.core.model.admin.Topic;
import rnn.core.model.admin.content.FileContent;
import rnn.core.service.admin.util.contentCreator.ContentCreator;
import rnn.core.service.filestorage.FileService;

import java.util.List;

@RequiredArgsConstructor
@Component
public class FileCreator implements ContentCreator {
    private final FileService fileService;

    @Override
    public Content create(Topic topic, ContentDTO contentDTO) {
        // TODO Добавить создание файла
        FileContentDTO fileContentDTO = (FileContentDTO) contentDTO;
        return FileContent
                .builder()
                .topic(topic)
                .type(fileContentDTO.getType())
                .fileUrl(fileContentDTO.getFileUrl())
                .position(fileContentDTO.getPosition())
                .type(fileContentDTO.getType())
                .build();
    }

    @Override
    public List<Content.Type> getType() {
        return List.of(Content.Type.PICTURE, Content.Type.VIDEO);
    }
}
