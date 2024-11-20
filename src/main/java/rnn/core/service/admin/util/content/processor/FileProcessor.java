package rnn.core.service.admin.util.content.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rnn.core.model.admin.Content;
import rnn.core.model.admin.Topic;
import rnn.core.model.admin.content.FileContent;
import rnn.core.model.admin.dto.ContentDTO;
import rnn.core.model.admin.dto.contentImpl.FileContentDTO;
import rnn.core.service.filestorage.FileService;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class FileProcessor<T> implements ContentProcessor<T> {
    private final FileService fileService;

    @Override
    public Content process(T entity, ContentDTO contentDTO) {
        FileContentDTO fileContentDTO = (FileContentDTO) contentDTO;

        FileContent content = FileContent
                .builder()
                .type(fileContentDTO.getType())
                .title(fileContentDTO.getTitle())
                .position(fileContentDTO.getPosition())
                .type(fileContentDTO.getType())
                .build();

        if (entity instanceof Topic topic) {
            content.setFileUrl(fileService.createContentFile(topic.getId(), UUID.randomUUID(), fileContentDTO.getFile()));
            content.setTopic(topic);
        }

        if (entity instanceof FileContent fileContent) {
            content.setId(fileContent.getId());
            content.setFileUrl(fileService.updateContentFile(fileContent, fileContentDTO.getFile()));
            content.setTopic(fileContent.getTopic());
        }

        return content;
    }

    @Override
    public List<Content.Type> getType() {
        return List.of(Content.Type.PICTURE, Content.Type.VIDEO);
    }
}
