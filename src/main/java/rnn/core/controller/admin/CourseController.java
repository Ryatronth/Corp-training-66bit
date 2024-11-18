package rnn.core.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rnn.core.model.admin.dto.CourseDTO;
import rnn.core.service.admin.CourseService;
import rnn.core.model.admin.Course;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class CourseController {
    private final CourseService courseService;

    @GetMapping("/courses")
    public ResponseEntity<Page<Course>> getCourses(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "limit", defaultValue = "20") int limit) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(courseService.findAll(page, limit));
    }

    @PostMapping("/courses")
    public ResponseEntity<Course> createCourse(@ModelAttribute CourseDTO courseDTO) {
        Course course = courseService.create(courseDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(course);
    }

    @PutMapping("/courses/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable long id, @ModelAttribute CourseDTO courseDTO) {
        Course course = courseService.update(id, courseDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(course);
    }

    @DeleteMapping("/courses/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable long id) {
        courseService.delete(id);
        return ResponseEntity.ok().build();
    }
}
