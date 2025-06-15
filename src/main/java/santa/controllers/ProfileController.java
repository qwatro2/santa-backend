package santa.controllers;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import santa.dtos.ProfileResponse;
import santa.entities.User;
import santa.exceptions.UserNotFoundException;
import santa.jwt.CustomUserDetails;
import santa.repositories.UserRepository;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final UserRepository userRepository;

    @Operation(summary = "Get profile info of current user")
    @GetMapping
    public ResponseEntity<ProfileResponse> getProfile(@AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new UserNotFoundException(currentUser.getId()));
        ProfileResponse response = ProfileResponse.fromEntity(user);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get profile info of current user")
    @GetMapping("/{userId}")
    public ResponseEntity<ProfileResponse> getProfile(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        ProfileResponse response = ProfileResponse.fromEntity(user);
        return ResponseEntity.ok(response);
    }
}
