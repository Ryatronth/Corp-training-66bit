package rnn.core.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import rnn.core.model.admin.dto.UserCourseGroupDTO;
import rnn.core.model.admin.dto.UserGroupDTO;
import rnn.core.model.security.User;
import rnn.core.model.security.repository.UserRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final UserRepository userRepository;

    public List<User> findAllWithoutCourse(long courseId) {
        return userRepository.findAllWithoutCourse(courseId);
    }

    public List<UserGroupDTO> findAllWithoutCourseOrInGroup(long courseId, long groupId) {
        return userRepository.findAllWithoutCourseOrInGroup(courseId, groupId);
    }

    public Page<UserCourseGroupDTO> findAllWithCourseAndGroup(long courseId, int page, int limit) {
        return userRepository.findAllWithCourseAndGroup(courseId, PageRequest.of(page, limit));
    }
}
