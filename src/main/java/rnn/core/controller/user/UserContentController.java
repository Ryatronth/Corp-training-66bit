package rnn.core.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rnn.core.model.user.UserContent;
import rnn.core.model.user.dto.UserContentDTO;
import rnn.core.service.user.UserContentService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserContentController {
    private final UserContentService userContentService;

    @GetMapping("/contents")
    public ResponseEntity<List<UserContentDTO>> getContent(
            @RequestParam(name = "userTopicId") long userTopicId,
            @RequestParam(name = "topicId") long topicId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userContentService.findByTopicId(userTopicId, topicId));
    }

    @PostMapping("/contents")
    public ResponseEntity<UserContent> createContent(
            @RequestParam(name = "userTopicId") long userTopicId,
            @RequestParam(name = "contentId") long contentId,
            @RequestParam(name = "currentAttempts") int currentAttempts,
            @RequestBody List<String> answers
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userContentService.answer(userTopicId, contentId, currentAttempts, answers));
    }
}
