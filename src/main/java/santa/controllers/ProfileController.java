package santa.controllers;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import santa.dtos.ChangePasswordRequest;
import santa.dtos.ProfileResponse;
import santa.jwt.CustomUserDetails;
import santa.services.ProfileService;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @Operation(summary = "Get profile info of current user")
    @GetMapping
    public ResponseEntity<ProfileResponse> getProfile(@AuthenticationPrincipal CustomUserDetails currentUser) {
        ProfileResponse response = profileService.getProfile(currentUser.getId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get profile info of current user")
    @GetMapping("/{userId}")
    public ResponseEntity<ProfileResponse> getProfile(@PathVariable Long userId) {
        ProfileResponse response = profileService.getProfile(userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Change password of current user")
    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordRequest request,
                                               @AuthenticationPrincipal CustomUserDetails currentUser) {
        profileService.changePassword(request, currentUser.getId());
        return ResponseEntity.ok().build();
    }
}
