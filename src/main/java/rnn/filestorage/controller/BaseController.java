package rnn.filestorage.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rnn.filestorage.service.FileService;

import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class BaseController {
    private final FileService fileService;

    @GetMapping("/courses/{uuid}")
    public ResponseEntity<Resource> getCourseFile(@PathVariable UUID uuid) throws IOException {
        Resource resource = fileService.getCourseFile(uuid);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(resource.getFile().toPath()))
                .body(resource);
    }

    @PostMapping("/courses/{uuid}")
    public ResponseEntity<String> uploadCourseFile(@PathVariable UUID uuid, @RequestParam("file") MultipartFile file) throws IOException {
        String resource = fileService.uploadCourseFile(uuid, file);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(resource);

    }
}
