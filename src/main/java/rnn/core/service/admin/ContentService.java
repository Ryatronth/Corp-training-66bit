package rnn.core.service.admin;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rnn.core.event.event.*;
import rnn.core.model.admin.Content;
import rnn.core.model.admin.Topic;
import rnn.core.model.admin.content.FileContent;
import rnn.core.model.admin.content.FreeformContent;
import rnn.core.model.admin.dto.ContentDTO;
import rnn.core.model.admin.repository.ContentRepository;
import rnn.core.service.admin.util.content.ContentManager;

import java.util.List;

@Service
public class ContentService extends PositionableService<Content, Long> {
    private final ApplicationEventPublisher eventPublisher;

    private final ContentManager contentManager;
    private final TopicService topicService;
    private final ContentRepository contentRepository;

    public ContentService(ContentRepository repository,
                          TopicService topicService,
                          ContentManager contentManager,
                          ApplicationEventPublisher eventPublisher
    ) {
        super(repository);
        this.topicService = topicService;
        this.contentRepository = repository;
        this.contentManager = contentManager;
        this.eventPublisher = eventPublisher;
    }

    @Override
    protected int getPosition(Content entity) {
        return entity.getPosition();
    }

    @Override
    protected void setPosition(Content entity, int position) {
        entity.setPosition(position);
    }

    @Override
    protected List<Content> findAllHigherOrEqualPosition(long parentId, int position) {
        return contentRepository.findAllWhichPositionIsHigherOrEqual(parentId, position);
    }

    @Override
    protected List<Content> findAllHigherPosition(long parentId, int position) {
        return contentRepository.findAllWhichPositionIsHigher(parentId, position);
    }

    @Override
    protected List<Content> findAllByParentId(long parentId) {
        return contentRepository.findByTopicIdOrderByPositionAsc(parentId);
    }

    @Transactional
    public Content create(long topicId, ContentDTO contentDTO) {
        Topic topic = topicService.find(topicId);
        Content content = contentManager.getCreator(contentDTO.getType()).process(topic, contentDTO);
        content = super.create(content, topic.getId(), content.getPosition());

        if (content instanceof FreeformContent freeformContent) {
            eventPublisher.publishEvent(new CreateContentEvent(this, topicId, freeformContent.getScore()));
        } else if (content instanceof FileContent fileContent) {
            eventPublisher.publishEvent(new RollbackFileContentEvent(this, fileContent.getFileUrl()));
        }

        return content;
    }

    @Transactional
    public Content update(long id, ContentDTO contentDTO) {
        Content content = findWithAnswersAndTopic(id);

        if (content.getType() != contentDTO.getType()) {
            throw new IllegalArgumentException("Несоответствие типов контента");
        }

        int score = 0;
        if (content instanceof FreeformContent freeformContent) {
            score = freeformContent.getScore();
        }

        Content updatedContent = contentManager.getUpdater(content.getType()).process(content, contentDTO);
        updatedContent = super.update(updatedContent, updatedContent.getPosition(), updatedContent.getTopic().getId());

        if (updatedContent instanceof FreeformContent freeformContent) {
            eventPublisher.publishEvent(
                    new UpdateContentEvent(
                            this,
                            freeformContent.getTopic().getId(),
                            freeformContent.getId(),
                            freeformContent.getScore() - score
                    )
            );
        } else if (updatedContent instanceof FileContent fileContent) {
            eventPublisher.publishEvent(new RollbackFileContentEvent(this, fileContent.getFileUrl()));
        }

        return updatedContent;
    }

    public List<Content> findByTopicIdWithAnswers(long topicId) {
        return contentRepository.findByTopicIdOrderByPositionAscFetchAnswers(topicId);
    }

    @Transactional
    public void delete(long id) {
        Content content = findWithAnswersAndTopic(id);

        if (content instanceof FreeformContent contentWithScore) {
            eventPublisher.publishEvent(
                    new DeleteContentEvent(
                            this,
                            content.getTopic().getId(),
                            content.getId(),
                            -contentWithScore.getScore()
                    )
            );
        } else if (content instanceof FileContent fileContent) {
            eventPublisher.publishEvent(new DeleteFileContentEvent(this, fileContent.getFileUrl()));
        }

        super.delete(content, content.getTopic().getId());
    }

    public Content findWithRightAnswers(long id) {
        return contentRepository.findByIdOrderByPositionAscFetchAnswers(id).orElseThrow(() -> new IllegalArgumentException("Контент с id = %s не найден".formatted(id)));
    }

    public Content findWithAnswersAndTopic(long id) {
        return contentRepository.findByIdFetchAnswersAndTopic(id).orElseThrow(() -> new IllegalArgumentException("Контент с id = %s не найден".formatted(id)));
    }
}
