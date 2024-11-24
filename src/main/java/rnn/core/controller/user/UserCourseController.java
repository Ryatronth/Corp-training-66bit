package rnn.core.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rnn.core.model.admin.Course;
import rnn.core.model.user.dto.UserCourseDTO;
import rnn.core.service.user.UserCourseService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserCourseController {
    private final UserCourseService userCourseService;

    @GetMapping("/courses")
    public ResponseEntity<List<UserCourseDTO>> getUserCourses(@RequestParam String username) {
        return ResponseEntity
                .status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userCourseService.findAllForUser(username));
    }

    @GetMapping("/courses/all")
    public ResponseEntity<List<Course>> getAllCourses(@RequestParam String username) {
        return ResponseEntity
                .status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userCourseService.findAllNotEnrolledByUser(username));

    }
}
