package rnn.core.service.admin;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rnn.core.event.event.DeleteTopicEvent;
import rnn.core.model.admin.Course;
import rnn.core.model.admin.dto.TopicDTO;
import rnn.core.model.admin.Module;
import rnn.core.model.admin.Topic;
import rnn.core.model.admin.repository.TopicRepository;

import java.util.List;

@Service
public class TopicService extends PositionableService<Topic, Long> {
    private final ApplicationEventPublisher eventPublisher;

    private final TopicRepository topicRepository;
    private final ModuleService moduleService;

    public TopicService(TopicRepository repository, ModuleService moduleService, ApplicationEventPublisher eventPublisher) {
        super(repository);
        this.topicRepository = repository;
        this.moduleService = moduleService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    protected int getPosition(Topic entity) {
        return entity.getPosition();
    }

    @Override
    protected void setPosition(Topic entity, int position) {
        entity.setPosition(position);
    }

    @Override
    protected List<Topic> findAllHigherOrEqualPosition(long parentId, int position) {
        return topicRepository.findAllWhichPositionIsHigherOrEqual(parentId, position);
    }

    @Override
    protected List<Topic> findAllHigherPosition(long parentId, int position) {
        return topicRepository.findAllWhichPositionIsHigher(parentId, position);
    }

    @Override
    protected List<Topic> findAllByParentId(long parentId) {
        return topicRepository.findByModuleIdOrderByPositionAsc(parentId);
    }

    @Transactional
    public Topic create(long moduleId, TopicDTO topicDTO) {
        Topic topic = Topic
                .builder()
                .title(topicDTO.title().trim())
                .module(moduleService.find(moduleId))
                .build();
        return super.create(topic, moduleId, topicDTO.position());
    }

    @Transactional
    public Topic update(long id, TopicDTO topicDTO) {
        Topic topic = find(id);

        topic.setTitle(topicDTO.title().trim());
        return super.update(topic, topicDTO.position(), topic.getModule().getId());
    }

    @Transactional
    public void delete(long id) {
        Topic topic = findWithModuleAndCourse(id);

        Module module = topic.getModule();
        module.setCountAnsweredContents(module.getCountAnsweredContents() - topic.getCountAnsweredContents());
        module.setScore(module.getScore() - topic.getScore());

        Course course = module.getCourse();
        course.setCountAnsweredContents(course.getCountAnsweredContents() - topic.getCountAnsweredContents());
        course.setScore(course.getScore() - topic.getScore());

        eventPublisher.publishEvent(new DeleteTopicEvent(this, id));

        super.delete(topic, module.getId());
    }

    public List<Topic> findByModuleId(long moduleId) {
        return findAllByParentId(moduleId);
    }

    public Topic find(long id) {
        return topicRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Тема с id = %s не найдена.".formatted(id)));
    }

    public Topic findWithModuleAndCourse(long id) {
        return topicRepository.findByIdWithModuleAndCourse(id).orElseThrow(() -> new IllegalArgumentException("Тема с id = %s не найдена.".formatted(id)));
    }
}
