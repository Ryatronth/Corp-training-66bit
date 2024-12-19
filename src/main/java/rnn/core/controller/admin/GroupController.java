package rnn.core.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rnn.core.model.admin.Group;
import rnn.core.model.admin.dto.DeadlineDTO;
import rnn.core.model.admin.dto.GroupDTO;
import rnn.core.model.admin.dto.GroupWithDeadlinesDTO;
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
                .body(groupService.findWithUsers(groupId));
    }

    @GetMapping("/groups/{groupId}/deadlines")
    public ResponseEntity<GroupWithDeadlinesDTO> getGroupWithDeadlines(@PathVariable long groupId) {
        return ResponseEntity
                .status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(groupService.findWithDeadline(groupId));
    }

    @PostMapping("/groups")
    public ResponseEntity<Group> createGroup(@RequestParam(name = "courseId") long courseId, @RequestBody GroupDTO dto) {
        return ResponseEntity
                .status(201)
                .contentType(MediaType.APPLICATION_JSON)
                .body(groupService.create(courseId, dto));
    }

    @PutMapping("/groups/users")
    public ResponseEntity<Group> addUsersGroup(
            @RequestParam(name = "courseId", required = false) Long courseId,
            @RequestParam(name = "groupId", required = false) Long groupId,
            @RequestParam(name = "default", required = false) boolean def,
            @RequestBody List<String> usernames
    ) {
        if (def) {
            return ResponseEntity
                    .status(200)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(groupService.addUsersInDefaultGroup(courseId, usernames));
        }
        return ResponseEntity
                .status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(groupService.updateUsers(groupId, usernames));
    }

    @PutMapping("/groups/users/move")
    public ResponseEntity<List<Group>> moveUsersGroup(
            @RequestParam(name = "destinationId") long destinationId,
            @RequestParam(name = "targetId") long targetId,
            @RequestBody List<String> usernames
    ) {
        return ResponseEntity
                .status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(groupService.moveUsers(destinationId, targetId, usernames));
    }

    @PutMapping("/groups/{groupId}/deadlines")
    public ResponseEntity<Group> addDeadlines(
            @PathVariable long groupId,
            @RequestBody List<DeadlineDTO> deadlines
    ) {
        return ResponseEntity
                .status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(groupService.changeDeadlines(groupId, deadlines));
    }

    @DeleteMapping("/groups/{groupId}")
    public ResponseEntity<Void> deleteAndMove(
            @RequestParam(name = "courseId") long courseId,
            @PathVariable(name = "groupId") long groupId,
            @RequestParam(name = "exclude") boolean exclude
    ) {
        if (exclude) {
            groupService.deleteAndExclude(courseId, groupId);
        } else {
            groupService.deleteAndMove(courseId, groupId);
        }
        return ResponseEntity
                .status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .build();
    }

    @DeleteMapping("/groups/users")
    public ResponseEntity<Void> deleteUsersFromGroup(
            @RequestParam(name = "courseId", required = false) Long courseId,
            @RequestParam(name = "groupId", required = false) Long groupId,
            @RequestBody List<String> usernames
    ) {
        groupService.deleteUsersFromGroup(courseId, groupId, usernames);
        return ResponseEntity
                .status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .build();
    }
}
