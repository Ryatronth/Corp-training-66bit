package rnn.core.base.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rnn.core.base.admin.dto.CourseCreationDTO;
import rnn.core.base.admin.service.CourseService;
import rnn.core.base.model.Course;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class CourseController {
    private final CourseService courseService;

    @GetMapping("/courses/{id}")
    public ResponseEntity<Course> getCourse(@PathVariable Long id) {
        Course course = courseService.get(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(course);
    }

    @PostMapping("/courses")
    public ResponseEntity<Course> createCourse(@ModelAttribute CourseCreationDTO courseDTO) {
        Course course = courseService.createAndSave(courseDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(course);
    }
}
