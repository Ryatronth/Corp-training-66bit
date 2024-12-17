package rnn.core.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

    public List<User> findAllWithCourse(long courseId) {
        return userRepository.findAllWithCourse(courseId);
    }
}
