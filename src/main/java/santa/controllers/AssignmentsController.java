package santa.controllers;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import santa.dtos.AssignmentDto;
import santa.entities.Assignment;
import santa.jwt.CustomUserDetails;
import santa.services.AssignmentService;
import santa.services.ShuffleService;

import java.util.UUID;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
public class AssignmentsController {
    private final ShuffleService shuffleService;
    private final AssignmentService assignmentService;

    @Operation(summary = "Create assignments")
    @PostMapping("/{groupId}")
    public ResponseEntity<Void> shuffleGroup(@PathVariable("groupId") UUID groupId) {
        shuffleService.shuffle(groupId);
        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Get assignment for the user who made the request")
    @GetMapping("/{groupId}")
    public ResponseEntity<AssignmentDto> getMyAssignment(@PathVariable("groupId") UUID groupId,
                                                         @AuthenticationPrincipal CustomUserDetails currentUser) {
        Assignment assignment = assignmentService.findByGroupAndSantaUser(
                groupId,
                currentUser.getId()
        );

        AssignmentDto response = new AssignmentDto(
                assignment.getId(),
                assignment.getReceiver().getUser().getId()
        );

        return ResponseEntity.ok(response);
    }
}
