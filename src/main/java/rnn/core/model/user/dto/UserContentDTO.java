package rnn.core.model.user.dto;

import rnn.core.model.admin.Content;
import rnn.core.model.user.UserContent;

public record UserContentDTO(Content content, UserContent userContent) {
}
