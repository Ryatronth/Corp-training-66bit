package rnn.core.model.user.dto;

import lombok.Builder;
import rnn.core.model.admin.GroupDeadline;
import rnn.core.model.admin.Module;
import rnn.core.model.admin.Topic;
import rnn.core.model.user.UserModule;
import rnn.core.model.user.UserTopic;

import java.util.List;

@Builder
public record UserModuleWithModuleDTO(Module module, UserModule userModule, List<TopicWithUserTopicDTO> topics, GroupDeadline deadline) {
    public record TopicWithUserTopicDTO(Topic topic, UserTopic userTopic) {}
}
