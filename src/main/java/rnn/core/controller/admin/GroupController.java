package rnn.core.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rnn.core.model.admin.Group;
import rnn.core.model.admin.dto.*;
import rnn.core.service.general.CourseSubscribeService;
import rnn.core.service.admin.GroupService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class GroupController {
    private final CourseSubscribeService courseSubscribeService;
    private final GroupService groupService;

    @GetMapping("/groups")
    public ResponseEntity<Page<Group>> getGroups(
            @RequestParam long courseId,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String direction,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int limit
    ) {
        return ResponseEntity
                .status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(groupService.findAll(courseId, sort, direction, page, limit));
    }

    @GetMapping("/groups/{groupId}/deadlines")
    public ResponseEntity<GroupWithDeadlinesDTO> getGroupWithDeadlines(@PathVariable long groupId) {
        return ResponseEntity
                .status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(groupService.findWithDeadline(groupId));
    }

    @PostMapping("/groups")
    public ResponseEntity<Group> createGroup(
            @RequestParam(name = "courseId") long courseId,
            @RequestBody GroupDTO dto
    ) {
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
                    .body(courseSubscribeService.subscribeCourse(courseId, usernames).group());
        }
        return ResponseEntity
                .status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(groupService.updateUsers(courseId, groupId, usernames));
    }

    @PutMapping("/groups/users/exclude")
    public ResponseEntity<Void> deleteUsersFromGroup(
            @RequestParam(name = "courseId") long courseId,
            @RequestBody List<ProcessGroupsUsersDTO> dtos
    ) {
        groupService.deleteUsersFromGroup(courseId, dtos);
        return ResponseEntity
                .status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .build();
    }

    @PutMapping("/groups/users/move")
    public ResponseEntity<Group> moveUsersGroup(
            @RequestParam(name = "targetId") long targetId,
            @RequestBody List<ProcessGroupsUsersDTO> dtos
    ) {
        return ResponseEntity
                .status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(groupService.moveUsers(targetId, dtos));
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
}
