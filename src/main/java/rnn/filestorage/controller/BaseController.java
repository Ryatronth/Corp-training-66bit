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

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class BaseController {
    private final FileService fileService;

    @GetMapping("/files")
    public ResponseEntity<Resource> getFile(@RequestParam(name = "path") String path, @RequestParam(name = "name") String name) throws IOException {
        Resource resource = fileService.getFile(path, name);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(resource.getFile().toPath()))
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(resource.getFile().length()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/files")
    public ResponseEntity<String> uploadFile(@RequestParam(name = "path") String path, @RequestParam(name = "name") String name, @RequestParam("file") MultipartFile file) throws IOException {
        String resource = fileService.uploadFile(path, name, file);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(resource);

    }

    @DeleteMapping("/files")
    public ResponseEntity<String> deleteFile(@RequestParam(name = "path") String path, @RequestParam(name = "name") String name) {
        fileService.deleteFile(path, name);
        return ResponseEntity.ok().build();
    }
}
