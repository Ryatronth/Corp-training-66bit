package rnn.core.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rnn.core.model.admin.dto.ContentDTO;
import rnn.core.service.admin.ContentService;
import rnn.core.model.admin.Content;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class ContentController {
    private final ContentService contentService;

    @GetMapping("/contents/{topicId}")
    public ResponseEntity<List<Content>> getAll(@PathVariable long topicId) {
        List<Content> contents = contentService.findByTopicId(topicId);
        return ResponseEntity
                .status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(contents);
    }

    @PostMapping("/contents/{topicId}")
    public ResponseEntity<Content> create(@PathVariable long topicId, @RequestBody ContentDTO dto) {
        return ResponseEntity
                .status(201)
                .contentType(MediaType.APPLICATION_JSON)
                .body(contentService.create(topicId, dto));
    }
}
