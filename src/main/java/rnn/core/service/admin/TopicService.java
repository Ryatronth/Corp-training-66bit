package rnn.core.service.admin;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rnn.core.model.admin.dto.TopicDTO;
import rnn.core.model.admin.Module;
import rnn.core.model.admin.Topic;
import rnn.core.model.admin.repository.TopicRepository;

import java.util.List;

@Service
public class TopicService extends PositionableService<Topic, Long> {
    private final TopicRepository topicRepository;
    private final ModuleService moduleService;

    public TopicService(TopicRepository repository, ModuleService moduleService) {
        super(repository);
        this.topicRepository = repository;
        this.moduleService = moduleService;
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
        Module module = moduleService.find(moduleId);

        Topic topic = Topic
                .builder()
                .title(topicDTO.title())
                .module(module)
                .build();

        return super.create(topic, moduleId, topicDTO.position());
    }

    @Transactional
    public Topic update(long id, TopicDTO topicDTO) {
        Topic topic = find(id);

        topic.setTitle(topicDTO.title());
        return super.update(topic, topicDTO.position(), topic.getModule().getId());
    }

    @Transactional
    public void delete(long id) {
        topicRepository.findById(id).ifPresent(existingTopic -> super.delete(existingTopic, existingTopic.getModule().getId()));
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
