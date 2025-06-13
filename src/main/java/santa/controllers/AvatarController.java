package santa.controllers;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import santa.dtos.AvatarDto;
import santa.jwt.CustomUserDetails;
import santa.services.AvatarService;

@RestController
@RequestMapping("/api/avatars")
@RequiredArgsConstructor
public class AvatarController {
    private final AvatarService avatarService;

    @Operation(summary = "Upload avatar")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadAvatar(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        avatarService.uploadAvatar(file, currentUser.getId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get avatar of current user")
    @GetMapping
    public ResponseEntity<Resource> getAvatar(@AuthenticationPrincipal CustomUserDetails currentUser) {
        AvatarDto dto = avatarService.getAvatarByUserId(currentUser.getId());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=avatar.%s", dto.extension()))
                .body(dto.resource());
    }

    @Operation(summary = "Get avatar of user by id")
    @GetMapping("/{userId}")
    public ResponseEntity<Resource> getAvatarById(@PathVariable Long userId) {
        AvatarDto dto = avatarService.getAvatarByUserId(userId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=avatar.%s", dto.extension()))
                .body(dto.resource());
    }
}
