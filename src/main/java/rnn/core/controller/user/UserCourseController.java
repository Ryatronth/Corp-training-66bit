package rnn.core.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rnn.core.service.user.UserCourseService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserCourseController {
    private final UserCourseService userCourseService;

    @GetMapping("/courses")
    public ResponseEntity<Page<?>> getUserCourses(
            @RequestParam(name = "username") String username,
            @RequestParam(name = "enrolled", required = false, defaultValue = "true") boolean enrolled,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "limit", required = false, defaultValue = "20") int limit
    ) {
        if (enrolled) {
            return ResponseEntity
                    .status(200)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(userCourseService.findAllByUsernameWithCourse(username, page, limit));
        }
        return ResponseEntity
                .status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userCourseService.findAllNotEnrolled(username, page, limit));
    }
}
