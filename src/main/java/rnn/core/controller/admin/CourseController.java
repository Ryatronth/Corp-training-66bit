package rnn.core.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rnn.core.util.CourseFilter;
import rnn.core.model.admin.dto.CourseWithImageDTO;
import rnn.core.model.admin.dto.CourseWithoutImageDTO;
import rnn.core.service.admin.CourseService;
import rnn.core.model.admin.Course;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class CourseController {
    private final CourseService courseService;

    @GetMapping("/courses")
    public ResponseEntity<Page<Course>> getCourses(
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "tags", required = false) List<String> tags,
            @RequestParam(name = "status", required = false, defaultValue = "ALL") CourseFilter filter,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "limit", required = false, defaultValue = "20") int limit
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(courseService.findAll(title, tags, filter, page, limit));
    }

    @GetMapping("/courses/{id}")
    public ResponseEntity<Course> getCourse(@PathVariable long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(courseService.find(id));
    }

    @PostMapping("/courses")
    public ResponseEntity<Course> createCourse(@ModelAttribute CourseWithImageDTO courseDTO) {
        Course course = courseService.create(courseDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(course);
    }

    @PutMapping("/courses/{id}/publish")
    public ResponseEntity<Course> publishCourse(
            @PathVariable long id,
            @RequestParam(name = "publish", defaultValue = "true") boolean publish
    ) {
        if (publish) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(courseService.publish(id));
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(courseService.unpublish(id));
    }

    @PutMapping("/courses/{id}/fields")
    public ResponseEntity<Course> updateCourseFields(
            @PathVariable long id,
            @ModelAttribute CourseWithoutImageDTO courseDTO
    ) {
        Course course = courseService.updateFields(id, courseDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(course);
    }

    @PutMapping("/courses/{id}/image")
    public ResponseEntity<Course> updateCourseImage(
            @PathVariable long id,
            @RequestPart(value = "image") MultipartFile image
    ) {
        Course course = courseService.updateImage(id, image);
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
