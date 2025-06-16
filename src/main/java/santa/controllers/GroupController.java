package santa.controllers;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import santa.dtos.*;
import santa.entities.SantaGroup;
import santa.jwt.CustomUserDetails;
import santa.services.GroupDetailsService;
import santa.services.GroupService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;
    private final GroupDetailsService groupDetailsService;

    @Operation(summary = "Get all organized groups")
    @GetMapping("/organized")
    public ResponseEntity<ListGroupsDto> getAllOrganized(@AuthenticationPrincipal CustomUserDetails currentUser) {
        List<SantaGroup> groups = groupService.getAllOrganizedByUser(currentUser.getId());
        List<GroupDto> dtos = groups.stream().map(GroupDto::fromEntity).toList();
        ListGroupsDto response = new ListGroupsDto(dtos, dtos.size());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all partition groups")
    @GetMapping("/partition")
    public ResponseEntity<ListGroupsDto> getAllPartition(@AuthenticationPrincipal CustomUserDetails currentUser) {
        List<SantaGroup> groups = groupService.getAllParticipatedByUser(currentUser.getId());
        List<GroupDto> dtos = groups.stream().map(GroupDto::fromEntity).toList();
        ListGroupsDto response = new ListGroupsDto(dtos, dtos.size());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create group")
    @PostMapping
    public ResponseEntity<GroupDto> createGroup(@RequestBody CreateGroupRequest request,
                                                @AuthenticationPrincipal CustomUserDetails currentUser) {
        SantaGroup group = groupService.createGroup(request, currentUser.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(GroupDto.fromEntity(group));
    }

    @Operation(summary = "Delete group")
    @DeleteMapping
    public ResponseEntity<Void> deleteGroup(@RequestBody DeleteGroupRequest request,
                                            @AuthenticationPrincipal CustomUserDetails currentUser) {
        groupService.deleteGroup(request.groupId(), currentUser.getId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get group details")
    @GetMapping("/{groupId}/details")
    public ResponseEntity<GroupDetailsDto> getDetails(@PathVariable("groupId") UUID groupId,
                                                      @AuthenticationPrincipal CustomUserDetails currentUser) {
        GroupDetailsDto response = groupDetailsService.getDetails(groupId, currentUser.getId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Add participant")
    @PostMapping("/{groupId}/participants")
    public ResponseEntity<Void> addParticipant(@PathVariable("groupId") UUID groupId,
                                               @AuthenticationPrincipal CustomUserDetails currentUser) {
        groupService.addParticipant(groupId, currentUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @Operation(summary = "Remove participant")
    @DeleteMapping("/{groupId}/participants")
    public ResponseEntity<Void> removeParticipant(@PathVariable("groupId") UUID groupId,
                                                  @RequestBody RemoveParticipantRequest request,
                                                  @AuthenticationPrincipal CustomUserDetails currentUser) {
        groupService.removeParticipant(request.participantId(), currentUser.getId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Add exclusion")
    @PostMapping("/{groupId}/exclusions")
    public ResponseEntity<Void> addExclusion(@PathVariable("groupId") UUID groupId,
                                             @RequestBody AddExclusionRequest request,
                                             @AuthenticationPrincipal CustomUserDetails currentUser) {
        groupService.addExclusion(
                currentUser.getId(),
                groupId,
                request.giverId(),
                request.receiverId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Delete exclusion")
    @DeleteMapping("/{groupId}/exclusions")
    public ResponseEntity<Void> deleteExclusion(@PathVariable("groupId") UUID groupId,
                                                @RequestBody DeleteExclusionRequest request,
                                                @AuthenticationPrincipal CustomUserDetails currentUser) {
        groupService.deleteExclusion(
                currentUser.getId(),
                groupId,
                request.exclusionId()
        );
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get exclusions")
    @GetMapping("/{groupId}/exclusions")
    public ResponseEntity<ListExclusionDto> getExclusions(@PathVariable("groupId") UUID groupId,
                                                          @AuthenticationPrincipal CustomUserDetails currentUser) {
        ListExclusionDto response = groupService.getExclusions(
                currentUser.getId(),
                groupId
        );
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get exclusion by id")
    @GetMapping("/{groupId}/exclusions/{exclusionId}")
    public ResponseEntity<ExclusionDto> getExclusionById(@PathVariable("groupId") UUID groupId,
                                                         @PathVariable("exclusionId") UUID exclusionId,
                                                         @AuthenticationPrincipal CustomUserDetails currentUser) {
        ExclusionDto response = groupService.getExclusion(
                currentUser.getId(),
                groupId,
                exclusionId
        );
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get join link")
    @GetMapping("/{groupId}")
    public ResponseEntity<JoinLinkResponse> getJoinLink(@PathVariable("groupId") UUID groupId,
                                                        @AuthenticationPrincipal CustomUserDetails currentUser) {
        JoinLinkResponse response = groupService.getJoinLink(groupId, currentUser.getId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Mark group done")
    @PostMapping("/{groupId}")
    public ResponseEntity<Void> markDone(@PathVariable("groupId") UUID groupId,
                                         @AuthenticationPrincipal CustomUserDetails currentUser) {
        groupService.markDone(groupId, currentUser.getId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get my participantId in group")
    @GetMapping("/{groupId}/participants/id")
    public ResponseEntity<ParticipantIdResponse> getParticipantId(
            @PathVariable("groupId") UUID groupId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        UUID id = groupService.getParticipantId(groupId, currentUser.getId());
        return ResponseEntity.ok(new ParticipantIdResponse(id));
    }

}
