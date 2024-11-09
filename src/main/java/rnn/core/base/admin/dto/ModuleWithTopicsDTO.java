package rnn.core.base.admin.dto;

import lombok.Builder;
import rnn.core.base.model.Topic;

import java.util.List;

@Builder
public record ModuleWithTopicsDTO(long id, int position, String title, int score, List<Topic> topics) {
}
