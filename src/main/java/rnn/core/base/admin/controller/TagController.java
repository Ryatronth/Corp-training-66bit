package rnn.core.base.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rnn.core.base.admin.dto.TagDTO;
import rnn.core.base.admin.service.TagService;
import rnn.core.base.model.Tag;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class TagController {
    private final TagService tagService;

    @GetMapping("/tags")
    public ResponseEntity<List<Tag>> getTags() {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(tagService.findAll());
    }

    @GetMapping("/tags/{name}")
    public ResponseEntity<Tag> getTag(@PathVariable String name) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(tagService.findOne(name));
    }

    @PostMapping("/tags")
    public ResponseEntity<Tag> createTag(@RequestBody TagDTO tag) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(tagService.create(tag));
    }

    @PutMapping("/tags/{name}")
    public ResponseEntity<Tag> updateTag(@PathVariable String name, @RequestBody TagDTO tag) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(tagService.update(name, tag));
    }

    @DeleteMapping("/tags/{name}")
    public ResponseEntity<Void> deleteTag(@PathVariable String name) {
        tagService.delete(name);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
