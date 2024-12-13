package rnn.core.model.admin.dto;

import lombok.Builder;
import rnn.core.model.admin.Group;
import rnn.core.model.security.User;

import java.util.List;

@Builder
public record GroupWithUsersDTO(Group group, List<User> users) {
}
