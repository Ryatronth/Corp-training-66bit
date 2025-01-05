package rnn.core.model.user.dto;

import rnn.core.model.user.UserContent;
import rnn.core.model.user.UserCourse;
import rnn.core.model.user.UserModule;
import rnn.core.model.user.UserTopic;

public record AnswerDTO(UserCourse course, UserModule module, UserTopic topic, UserContent content) {
}
