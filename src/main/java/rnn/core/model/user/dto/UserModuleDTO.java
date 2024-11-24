package rnn.core.model.user.dto;

import lombok.Builder;
import rnn.core.model.admin.GroupDeadline;
import rnn.core.model.admin.Module;

@Builder
public record UserModuleDTO(long id, int currentScore, GroupDeadline deadline, Module module) {
}
