package rnn.core.base.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rnn.core.base.admin.dto.TopicDTO;
import rnn.core.base.admin.dto.mapper.TopicMapper;
import rnn.core.base.model.Module;
import rnn.core.base.model.Topic;
import rnn.core.base.model.repository.TopicRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TopicService {
    private final TopicRepository topicRepository;
    private final TopicMapper topicMapper;
    private final ModuleService moduleService;

    @Transactional
    public Topic create(long moduleID, TopicDTO topicDTO) {
        Module module = moduleService.find(moduleID);
        Topic topic = topicMapper.fromDTO(topicDTO);
        topic.setModule(module);

        List<Topic> topics = topicRepository.findAllWhichPositionIsHigherOrEqual(topic.getModule().getId(), topic.getPosition());
        for (Topic t : topics) {
            t.setPosition(t.getPosition() + 1);
        }

        topicRepository.saveAllAndFlush(topics);

        return topicRepository.saveAndFlush(topic);
    }

    @Transactional
    public Topic update(long id, TopicDTO topicDTO) {
        Topic topic = find(id);

        int delta = topic.getPosition() - topicDTO.position();

        if (delta != 0) {
            List<Topic> topics = findByModuleId(topic.getModule().getId());

            for (Topic t : topics) {
                if (t.getId() == topic.getId()) {
                    continue;
                }

                if (delta < 0 && t.getPosition() > topic.getPosition() && t.getPosition() <= topicDTO.position()) {
                    t.setPosition(t.getPosition() - 1);
                }

                if (delta > 0 && t.getPosition() > topicDTO.position() && t.getPosition() <= topic.getPosition()) {
                    t.setPosition(t.getPosition() + 1);
                }
            }

            topicRepository.saveAllAndFlush(topics);

            topic.setPosition(topic.getPosition());
        }

        topic = topicMapper.updateFromDTO(topic, topicDTO);
        return topicRepository.saveAndFlush(topic);
    }

    @Transactional
    public void delete(long id) {
        topicRepository.findById(id).ifPresent(existingTopic -> {
            List<Topic> topics = topicRepository.findAllWhichPositionIsHigher(existingTopic.getModule().getId(), existingTopic.getPosition());

            for (Topic t : topics) {
                t.setPosition(t.getPosition() - 1);
            }

            topicRepository.delete(existingTopic);

            topicRepository.saveAllAndFlush(topics);
        });
    }

    public List<Topic> findByModuleId(long moduleId) {
        return topicRepository.findByModuleId(moduleId);
    }

    public Topic find(long id) {
        return topicRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Тема с id = %s не найдена.".formatted(id)));
    }
}
