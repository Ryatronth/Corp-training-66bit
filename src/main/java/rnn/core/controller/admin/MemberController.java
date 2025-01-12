package rnn.core.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rnn.core.model.admin.dto.UserCourseGroupDTO;
import rnn.core.model.admin.dto.UserGroupDTO;
import rnn.core.model.security.User;
import rnn.core.service.admin.MemberService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/members/groups/current")
    public ResponseEntity<Page<UserGroupDTO>> findAllWithoutCourseOrInGroupOrInDefault(
            @RequestParam long courseId,
            @RequestParam long groupId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int limit
    ) {
        return ResponseEntity
                .status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(memberService.findAllWithoutCourseOrInGroupOrInDefault(courseId, groupId, name, page, limit));
    }

    @GetMapping("/members/groups/new")
    public ResponseEntity<Page<User>> findAllWithoutCourseOrInDefault(
            @RequestParam long courseId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int limit
    ) {
        return ResponseEntity
                .status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(memberService.findAllWithoutCourseOrInDefault(courseId, name, page, limit));
    }

    @GetMapping("/members/groups/all")
    public ResponseEntity<Page<UserCourseGroupDTO>> findAllWithCourseAndGroup(
            @RequestParam long courseId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String direction,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int limit
    ) {
        return ResponseEntity
                .status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(memberService.findAllWithCourseAndGroup(courseId, name, sort, direction, page, limit));
    }

    @GetMapping("/members/groups/exclude")
    public ResponseEntity<Page<User>> findAllWithoutCourse(
            @RequestParam long courseId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int limit
    ) {
        return ResponseEntity
                .status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(memberService.findAllWithoutCourse(courseId, name, page, limit));
    }
}
