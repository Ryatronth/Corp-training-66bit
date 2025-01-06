package rnn.core.service.admin;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rnn.core.model.admin.Content;
import rnn.core.model.admin.Course;
import rnn.core.model.admin.Module;
import rnn.core.model.admin.Topic;
import rnn.core.model.admin.content.FreeformContent;
import rnn.core.model.admin.dto.ContentDTO;
import rnn.core.model.admin.repository.ContentRepository;
import rnn.core.service.admin.util.content.ContentManager;

import java.util.List;

@Service
public class ContentService extends PositionableService<Content, Long> {
    private final ContentManager contentManager;
    private final TopicService topicService;
    private final ContentRepository contentRepository;

    public ContentService(ContentRepository repository,
                          TopicService topicService,
                          ContentManager contentManager
    ) {
        super(repository);
        this.topicService = topicService;
        this.contentRepository = repository;
        this.contentManager = contentManager;
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
        Topic topic = topicService.findWithModuleAndCourse(topicId);
        Content content = contentManager.getCreator(contentDTO.getType()).process(topic, contentDTO);
        content = super.create(content, topic.getId(), content.getPosition());

        if (content instanceof FreeformContent contentWithScore) {
            topic.setCountAnsweredContents(topic.getCountAnsweredContents() + 1);
            topic.setScore(contentWithScore.getScore() + topic.getScore());

            Module module = topic.getModule();
            module.setScore(contentWithScore.getScore() + module.getScore());

            Course course = module.getCourse();
            course.setScore(contentWithScore.getScore() + course.getScore());
        }

        return content;
    }

    @Transactional
    public Content update(long id, ContentDTO contentDTO) {
        Content content = findWithAnswersAndTopicAndModuleAndCourse(id);

        if (content.getType() != contentDTO.getType()) {
            throw new IllegalArgumentException("Несоответствие типов контента");
        }

        Content updatedContent = contentManager.getUpdater(content.getType()).process(content, contentDTO);
        updatedContent = super.update(updatedContent, updatedContent.getPosition(), updatedContent.getTopic().getId());

        if (updatedContent instanceof FreeformContent contentWithScore
                && content instanceof FreeformContent oldContent
                && contentWithScore.getScore() != oldContent.getScore()
        ) {
            Topic topic = updatedContent.getTopic();
            topic.setScore(contentWithScore.getScore() + topic.getScore() - oldContent.getScore());

            Module module = topic.getModule();
            module.setScore(contentWithScore.getScore() + module.getScore() - oldContent.getScore());

            Course course = module.getCourse();
            course.setScore(contentWithScore.getScore() + course.getScore() - oldContent.getScore());
        }

        return updatedContent;
    }

    public List<Content> findByTopicIdWithAnswers(long topicId) {
        return contentRepository.findByTopicIdOrderByPositionAscWithAnswers(topicId);
    }

    @Transactional
    public void delete(long id) {
        Content content = findWithAnswersAndTopicAndModuleAndCourse(id);

        if (content instanceof FreeformContent contentWithScore) {
            Topic topic = contentWithScore.getTopic();
            topic.setScore(topic.getScore() - contentWithScore.getScore());

            Module module = topic.getModule();
            module.setScore(module.getScore() - contentWithScore.getScore());

            Course course = module.getCourse();
            course.setScore(course.getScore() - contentWithScore.getScore());
        }

        super.delete(content, content.getTopic().getId());
    }

    public Content findWithRightAnswers(long id) {
        return contentRepository.findByIdOrderByPositionAscWithAnswers(id).orElseThrow(() -> new IllegalArgumentException("Контент с id = %s не найден".formatted(id)));
    }

    public Content findWithAnswersAndTopicAndModuleAndCourse(long id) {
        return contentRepository.findByIdWithAnswersAndTopicAndModuleAndCourse(id).orElseThrow(() -> new IllegalArgumentException("Контент с id = %s не найден".formatted(id)));
    }
}
