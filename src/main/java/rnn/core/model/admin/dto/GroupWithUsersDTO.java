package rnn.core.model.admin.dto;

import lombok.Builder;
import rnn.core.model.user.UserCourse;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record GroupWithUsersDTO(long id, String name, LocalDateTime startTime, LocalDateTime endTime, List<UserCourse> course) {
}
