package rnn.core.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rnn.core.controller.admin.filter.CourseFilter;
import rnn.core.model.user.dto.UserCourseWithCourseAndGroupDTO;
import rnn.core.service.user.UserCourseService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserCourseController {
    private final UserCourseService userCourseService;

    @GetMapping("/courses")
    public ResponseEntity<Page<?>> getUserCourses(
            @RequestParam(name = "username") String username,
            @RequestParam(name = "enrolled", required = false, defaultValue = "true") boolean enrolled,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "tags", required = false) List<String> tags,
            @RequestParam(name = "status", required = false, defaultValue = "ALL") CourseFilter filter,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "limit", required = false, defaultValue = "20") int limit
    ) {
        if (enrolled) {
            return ResponseEntity
                    .status(200)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(userCourseService.findAllByUsernameWithCourse(username, title, tags, filter, page, limit));
        }
        return ResponseEntity
                .status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userCourseService.findAllNotEnrolled(username, title, tags, filter, page, limit));
    }

    @GetMapping("/courses/{id}")
    public ResponseEntity<UserCourseWithCourseAndGroupDTO> getCourse(
            @PathVariable long id,
            @RequestParam(name = "username") String username
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userCourseService.findWithCourseAndGroup(id, username));
    }
}
