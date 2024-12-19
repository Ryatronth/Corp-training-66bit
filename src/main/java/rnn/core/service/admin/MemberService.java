package rnn.core.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rnn.core.model.admin.dto.UserCourseGroupDTO;
import rnn.core.model.admin.dto.UserGroupDTO;
import rnn.core.model.security.repository.UserRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final UserRepository userRepository;

    public List<UserGroupDTO> findAllWithoutCourseOrInGroup(long courseId, long groupId) {
        return userRepository.findAllWithoutCourseOrInGroup(courseId, groupId);
    }

    public List<UserCourseGroupDTO> findAllWithCourseAndGroup(long courseId) {
        return userRepository.findAllWithCourseAndGroup(courseId);
    }
}
