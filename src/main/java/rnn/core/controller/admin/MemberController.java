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

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/members/groups/current")
    public ResponseEntity<List<UserGroupDTO>> getAllWithoutCourseOrInGroup(
            @RequestParam long courseId,
            @RequestParam long groupId
    ) {
        return ResponseEntity
                .status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(memberService.findAllWithoutCourseOrInGroup(courseId, groupId));
    }

    @GetMapping("/members/groups/new")
    public ResponseEntity<List<User>> getAllWithoutCourse(
            @RequestParam long courseId
    ) {
        return ResponseEntity
                .status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(memberService.findAllWithoutCourse(courseId));
    }

    @GetMapping("/members/groups/all")
    public ResponseEntity<Page<UserCourseGroupDTO>> findAllWithUserCourseAndGroup(
            @RequestParam long courseId,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int limit
    ) {
        return ResponseEntity
                .status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(memberService.findAllWithCourseAndGroup(courseId, page, limit));
    }
}
