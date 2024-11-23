package rnn.core.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rnn.core.model.admin.Group;
import rnn.core.model.admin.dto.GroupDTO;
import rnn.core.model.admin.dto.GroupWithUsersDTO;
import rnn.core.service.admin.GroupService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class GroupController {
    private final GroupService groupService;

    @GetMapping("/groups")
    public ResponseEntity<List<Group>> getGroups(@RequestParam long courseId) {
        return ResponseEntity
                .status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(groupService.findAll(courseId));
    }

    @GetMapping("/groups/{groupId}/users")
    public ResponseEntity<GroupWithUsersDTO> getGroupWithUsers(@PathVariable long groupId) {
        return ResponseEntity
                .status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(groupService.findByIdWithUsers(groupId));
    }

    @PostMapping("/groups")
    public ResponseEntity<Group> createGroup(@RequestParam(name = "courseId") long courseId, @RequestBody GroupDTO dto) {
        return ResponseEntity
                .status(201)
                .contentType(MediaType.APPLICATION_JSON)
                .body(groupService.create(courseId, dto));
    }

    @PutMapping("/groups/{groupId}/users")
    public ResponseEntity<Group> addUsers(@PathVariable long groupId, @RequestBody List<String> usernames) {
        return ResponseEntity
                .status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(groupService.addUsers(groupId, usernames));
    }

    @DeleteMapping("/groups/{groupId}")
    public ResponseEntity<Void> deleteGroup(@PathVariable long groupId) {
        groupService.delete(groupId);
        return ResponseEntity.ok().build();
    }
}
