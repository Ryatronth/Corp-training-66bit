package rnn.core.model.admin.dto;

import rnn.core.model.admin.Group;

public record MoveGroupsDTO(Group destination, Group target) {
}
