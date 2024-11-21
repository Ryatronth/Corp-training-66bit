package rnn.core.controller.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rnn.core.model.admin.Content;
import rnn.core.model.admin.dto.ContentDTO;
import rnn.core.model.admin.dto.contentImpl.FileContentDTO;
import rnn.core.service.admin.ContentService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class ContentController {
    private final ContentService contentService;
    private final ObjectMapper objectMapper;

    @GetMapping("/contents")
    public ResponseEntity<List<Content>> findAll(@RequestParam long topicId) {
        List<Content> contents = contentService.findByTopicId(topicId);
        return ResponseEntity
                .status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(contents);
    }

    @PostMapping(value = "/contents", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Content> create(
            @RequestParam long topicId,
            @RequestPart(value = "content") String json,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws JsonProcessingException {
        ContentDTO contentDTO = objectMapper.readValue(json, ContentDTO.class);

        if (contentDTO instanceof FileContentDTO) {
            ((FileContentDTO) contentDTO).setFile(file);
        }

        return ResponseEntity
                .status(201)
                .contentType(MediaType.APPLICATION_JSON)
                .body(contentService.create(topicId, contentDTO));
    }

    @PutMapping(value = "/contents/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Content> update(
            @PathVariable long id,
            @RequestPart(value = "content") String json,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws JsonProcessingException {
        ContentDTO contentDTO = objectMapper.readValue(json, ContentDTO.class);

        if (contentDTO instanceof FileContentDTO) {
            ((FileContentDTO) contentDTO).setFile(file);
        }

        return ResponseEntity
                .status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(contentService.update(id, contentDTO));
    }

    @DeleteMapping("/contents/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        contentService.delete(id);
        return ResponseEntity.ok().build();
    }
}
