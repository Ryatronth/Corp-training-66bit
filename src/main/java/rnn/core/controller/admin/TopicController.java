package rnn.core.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rnn.core.model.admin.dto.TopicDTO;
import rnn.core.service.admin.TopicService;
import rnn.core.model.admin.Topic;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class TopicController {
    private final TopicService topicService;

    @GetMapping("/topics")
    public ResponseEntity<List<Topic>> getModuleTopics(@RequestParam("moduleId") long moduleId) {
        List<Topic> topics = topicService.findByModuleId(moduleId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(topics);
    }

    @PostMapping("/topics")
    public ResponseEntity<Topic> createTopic(@RequestParam("moduleId") long moduleId, @Valid @RequestBody TopicDTO topicDTO) {
        Topic topic = topicService.create(moduleId, topicDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(topic);
    }

    @PutMapping("/topics/{id}")
    public ResponseEntity<Topic> updateTopic(@PathVariable long id, @Valid @RequestBody TopicDTO topicDTO) {
        Topic module = topicService.update(id, topicDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(module);
    }

    @DeleteMapping("/topics/{id}")
    public ResponseEntity<Void> deleteTopic(@PathVariable long id) {
        topicService.delete(id);
        return ResponseEntity.ok().build();
    }
}
