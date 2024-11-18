package rnn.core.model.admin.dto;

import lombok.Builder;
import rnn.core.model.admin.Topic;

import java.util.List;

@Builder
public record ModuleWithTopicsDTO(long id, int position, String title, int score, List<Topic> topics) {
}
