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
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class FileCreator implements ContentCreator {
    private final FileService fileService;

    private static final String PATH_TO_PICTURE = "picture";
    private static final String PATH_TO_VIDEO = "video";

    @Override
    public Content create(Topic topic, ContentDTO contentDTO) {
        FileContentDTO fileContentDTO = (FileContentDTO) contentDTO;

        String pathToFile;
        if (fileContentDTO.getType() == Content.Type.PICTURE) {
            pathToFile = PATH_TO_PICTURE;
        } else {
            pathToFile = PATH_TO_VIDEO;
        }

        pathToFile = "%s/%s".formatted(pathToFile, topic.getId());
        return FileContent
                .builder()
                .topic(topic)
                .type(fileContentDTO.getType())
                .title(fileContentDTO.getTitle())
                .fileUrl(fileService.createContentFile(pathToFile, UUID.randomUUID(), fileContentDTO.getFile()))
                .position(fileContentDTO.getPosition())
                .type(fileContentDTO.getType())
                .build();
    }

    @Override
    public List<Content.Type> getType() {
        return List.of(Content.Type.PICTURE, Content.Type.VIDEO);
    }
}
