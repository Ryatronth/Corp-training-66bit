package rnn.core.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import rnn.core.model.admin.dto.UserCourseGroupDTO;
import rnn.core.model.admin.dto.UserGroupDTO;
import rnn.core.model.security.User;
import rnn.core.model.security.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final UserRepository userRepository;

    public Page<User> findAllWithoutCourse(long courseId, int page, int limit) {
        return userRepository.findAllWithoutCourse(courseId, PageRequest.of(page, limit));
    }

    public Page<User> findAllWithoutCourseOrInDefault(long courseId, int page, int limit) {
        return userRepository.findAllWithoutCourseOrInDefault(courseId, PageRequest.of(page, limit));
    }

    public Page<UserGroupDTO> findAllWithoutCourseOrInGroupOrInDefault(long courseId, long groupId, int page, int limit) {
        return userRepository.findAllWithoutCourseOrInGroupOrInDefault(courseId, groupId, PageRequest.of(page, limit));
    }

    public Page<UserCourseGroupDTO> findAllWithCourseAndGroup(long courseId, int page, int limit) {
        return userRepository.findAllWithCourseAndGroup(courseId, PageRequest.of(page, limit));
    }
}
