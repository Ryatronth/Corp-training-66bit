package rnn.core.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rnn.core.model.user.dto.UserModuleDTO;
import rnn.core.service.user.UserModuleService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserModuleController {
    private final UserModuleService userModuleService;

    @GetMapping("/modules")
    public ResponseEntity<List<UserModuleDTO>> getCourseModules(
            @RequestParam(name = "courseId") long courseId,
            @RequestParam(name = "groupId") long groupId,
            @RequestParam(name = "userCourseId") long userCourseId
    ) {
        return ResponseEntity
                .status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userModuleService.findAllByCourseId(courseId, groupId, userCourseId));
    }

    @PostMapping("/modules/{id}")
    public ResponseEntity<UserModuleDTO> createCourseModules(
            @PathVariable long id,
            @RequestParam(name = "courseId") long courseId,
            @RequestParam(name = "groupId") long groupId
    ) {
        return ResponseEntity
                .status(201)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userModuleService.create(id, courseId, groupId));
    }
}
