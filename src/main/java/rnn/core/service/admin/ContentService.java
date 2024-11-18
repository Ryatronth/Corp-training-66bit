package rnn.core.service.admin;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rnn.core.model.admin.dto.ContentDTO;
import rnn.core.service.admin.util.contentCreator.ContentFactory;
import rnn.core.model.admin.Content;
import rnn.core.model.admin.Topic;
import rnn.core.model.admin.repository.ContentRepository;

import java.util.List;

@Service
public class ContentService extends PositionableService<Content, Long> {
    private final ContentFactory contentFactory;
    private final TopicService topicService;
    private final ContentRepository contentRepository;

    public ContentService(ContentRepository repository,
                          TopicService topicService,
                          ContentFactory contentFactory
    ) {
        super(repository);
        this.topicService = topicService;
        this.contentRepository = repository;
        this.contentFactory = contentFactory;
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
        Content content = contentFactory.getCreator(contentDTO.getType()).create(topic, contentDTO);
        return super.create(content, topic.getId(), content.getPosition());
    }

    @Transactional
    public List<Content> findByTopicId(long topicId) {
        List<Content> contentsWithAnswers = contentRepository.findByTopicIdOrderByPositionAscWithAnswers(topicId);
        return contentRepository.findByTopicIdOrderByPositionAscWithQuestions(topicId);
    }
}
