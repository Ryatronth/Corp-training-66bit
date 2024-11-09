package rnn.core.base.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rnn.core.base.admin.dto.ModuleDTO;
import rnn.core.base.admin.dto.ModuleWithTopicsDTO;
import rnn.core.base.admin.service.ModuleService;
import rnn.core.base.model.Module;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class ModuleController {
    private final ModuleService moduleService;

    @GetMapping("/modules")
    public ResponseEntity<List<ModuleWithTopicsDTO>> getCourseModules(@RequestParam("courseId") long courseId) {
        List<ModuleWithTopicsDTO> modules = moduleService.findByCourseIdWithTopics(courseId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(modules);
    }

    @PostMapping("/modules")
    public ResponseEntity<Module> createModule(@RequestParam("courseId") long courseId, @RequestBody ModuleDTO moduleDTO) {
        Module module = moduleService.create(courseId, moduleDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(module);
    }

    @PutMapping("/modules/{id}")
    public ResponseEntity<Module> updateModule(@PathVariable long id, @RequestBody ModuleDTO moduleDTO) {
        Module module = moduleService.update(id, moduleDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(module);
    }

    @DeleteMapping("/modules/{id}")
    public ResponseEntity<Void> deleteModule(@PathVariable long id) {
        moduleService.delete(id);
        return ResponseEntity.ok().build();
    }
}
