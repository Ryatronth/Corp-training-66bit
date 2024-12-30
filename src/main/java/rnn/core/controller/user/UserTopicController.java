package rnn.core.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rnn.core.model.user.UserTopic;
import rnn.core.service.user.UserTopicService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserTopicController {
    private final UserTopicService userTopicService;

    @PostMapping("/topics")
    public ResponseEntity<UserTopic> create(
            @RequestParam(name = "userModuleId") long userModuleId,
            @RequestParam(name = "topicId") long topicId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userTopicService.create(userModuleId, topicId));
    }
}
