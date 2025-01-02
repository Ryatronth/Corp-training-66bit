package rnn.core.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rnn.core.model.user.UserModule;
import rnn.core.model.user.dto.UserModuleWithModuleDTO;
import rnn.core.service.user.UserModuleService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserModuleController {
    private final UserModuleService userModuleService;

    @GetMapping("/modules")
    public ResponseEntity<List<UserModuleWithModuleDTO>> findByCourseId(
            @RequestParam(name = "courseId") long courseId,
            @RequestParam(name = "userCourseId") long userCourseId,
            @RequestParam(name = "groupId") long groupId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userModuleService.findAllWithModuleAndDeadline(courseId, userCourseId, groupId));
    }

    @PostMapping("/modules")
    public ResponseEntity<UserModule> createUserModule(
            @RequestParam(name = "courseId") long userCourseId,
            @RequestParam(name = "moduleId") long moduleId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userModuleService.create(userCourseId, moduleId));
    }
}
