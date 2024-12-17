package rnn.core.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rnn.core.model.security.User;
import rnn.core.service.admin.MemberService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/members")
    public List<User> getAllMembersInCourse(@RequestParam long courseId, @RequestParam boolean inclusive) {
        if (inclusive) {
            return memberService.findAllWithCourse(courseId);
        }
        return memberService.findAllWithoutCourse(courseId);
    }
}
