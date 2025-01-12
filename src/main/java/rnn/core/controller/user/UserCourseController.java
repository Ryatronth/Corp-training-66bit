package rnn.core.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rnn.core.model.user.dto.UserCourseDTO;
import rnn.core.util.CourseFilter;
import rnn.core.model.general.dto.SubscribeCourseDTO;
import rnn.core.model.user.UserCourse;
import rnn.core.model.user.dto.UserCourseWithCourseAndGroupDTO;
import rnn.core.service.general.CourseSubscribeService;
import rnn.core.service.user.UserCourseService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserCourseController {
    private final CourseSubscribeService courseSubscribeService;
    private final UserCourseService userCourseService;

    @GetMapping("/courses")
    public ResponseEntity<Page<UserCourseDTO>> getUserCourses(
            @RequestParam(name = "userId") long userId,
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
                    .body(userCourseService.findAllByUserIdWithCourse(userId, title, tags, filter, page, limit));
        }
        return ResponseEntity
                .status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userCourseService.findAllNotEnrolled(userId, title, tags, filter, page, limit));
    }

    @GetMapping("/courses/current")
    public ResponseEntity<UserCourse> getUserCourses(
            @RequestParam(name = "courseId") long courseId,
            @RequestParam(name = "userId") long userId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userCourseService.findByCourseIdAndUserIds(courseId, userId));
    }

    @GetMapping("/courses/{id}")
    public ResponseEntity<UserCourseWithCourseAndGroupDTO> getCourse(
            @PathVariable long id,
            @RequestParam(name = "userId") long userId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userCourseService.findWithCourseAndGroup(id, userId));
    }

    @PostMapping("/courses/subscribe")
    public ResponseEntity<SubscribeCourseDTO> subscribe(
            @RequestParam(name = "courseId") long courseId,
            @RequestBody List<Long> userIds
    ) {
        return ResponseEntity
                .status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(courseSubscribeService.subscribeCourse(courseId, userIds));
    }
}
